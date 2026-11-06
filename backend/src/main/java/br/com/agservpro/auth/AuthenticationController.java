package br.com.agservpro.auth;

import br.com.agservpro.security.AgServProUserDetails;
import br.com.agservpro.security.TenantRlsContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationService authenticationService;
    private final JdbcTemplate jdbcTemplate;
    private final TenantRlsContext rlsContext;
    private final CookieCsrfTokenRepository csrfTokenRepository;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            AuthenticationService authenticationService,
            JdbcTemplate jdbcTemplate,
            TenantRlsContext rlsContext,
            CookieCsrfTokenRepository csrfTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.authenticationService = authenticationService;
        this.jdbcTemplate = jdbcTemplate;
        this.rlsContext = rlsContext;
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @GetMapping("/csrf")
    public CsrfResponse csrf(CsrfToken csrfToken) {
        return new CsrfResponse(csrfToken.getToken());
    }

    @PostMapping("/session")
    public SessionResponse login(
            @Valid @RequestBody LoginRequest login,
            HttpServletRequest request,
            HttpServletResponse response) {
        Authentication authentication;
        try {
            if (login.password().getBytes(StandardCharsets.UTF_8).length > 72) {
                throw new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas.");
            }
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(login.email().trim().toLowerCase(Locale.ROOT), login.password()));
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas.");
        }

        request.getSession(true);
        new ChangeSessionIdAuthenticationStrategy().onAuthentication(authentication, request, response);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        csrfTokenRepository.saveToken(null, request, response);

        AgServProUserDetails user = (AgServProUserDetails) authentication.getPrincipal();
        return new SessionResponse(user.id(), user.email(), user.papelPlataforma());
    }

    @DeleteMapping("/session")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        SecurityContextHolder.clearContext();
        csrfTokenRepository.saveToken(null, request, response);
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public CurrentUserResponse currentUser(Authentication authentication) {
        AgServProUserDetails user = (AgServProUserDetails) authentication.getPrincipal();
        if ("ADMIN_SAAS".equals(user.papelPlataforma())) {
            return new CurrentUserResponse(user.id(), user.email(), user.papelPlataforma(), List.of());
        }

        rlsContext.useUsuario(user.id());
        List<MembershipResponse> memberships = jdbcTemplate.query(
                """
                SELECT pu.prestadora_id, p.nome, p.status AS prestadora_status, pu.papel, pu.status
                FROM prestadora_usuario pu
                JOIN prestadora p ON p.id = pu.prestadora_id
                WHERE pu.usuario_id = ?
                ORDER BY p.nome
                """,
                (resultSet, rowNumber) -> new MembershipResponse(
                        resultSet.getObject("prestadora_id", UUID.class),
                        resultSet.getString("nome"),
                        resultSet.getString("prestadora_status"),
                        resultSet.getString("papel"),
                        resultSet.getString("status")),
                user.id());
        return new CurrentUserResponse(user.id(), user.email(), null, memberships);
    }

    @PostMapping("/invitations/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvitation(@Valid @RequestBody AcceptInvitationRequest request) {
        authenticationService.acceptInvitation(request.token(), request.name(), request.password());
    }

    public record LoginRequest(@Email @NotBlank @Size(max = 254) String email, @NotBlank @Size(max = 72) String password) {}
    public record AcceptInvitationRequest(@NotBlank @Size(max = 128) String token, @NotBlank @Size(max = 160) String name, @NotBlank @Size(min = 12, max = 72) String password) {}
    public record CsrfResponse(String token) {}
    public record SessionResponse(UUID id, String email, String papelPlataforma) {}
    public record CurrentUserResponse(UUID id, String email, String papelPlataforma, List<MembershipResponse> prestadoras) {}
    public record MembershipResponse(UUID prestadoraId, String prestadora, String prestadoraStatus, String papel, String status) {}
}
