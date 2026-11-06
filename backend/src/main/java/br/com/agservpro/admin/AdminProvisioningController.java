package br.com.agservpro.admin;

import br.com.agservpro.security.AgServProUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/prestadoras")
public class AdminProvisioningController {
    private final AdminProvisioningService provisioningService;

    public AdminProvisioningController(AdminProvisioningService provisioningService) {
        this.provisioningService = provisioningService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProvisionResponse provision(@Valid @RequestBody ProvisionRequest request, Authentication authentication) {
        AgServProUserDetails actor = (AgServProUserDetails) authentication.getPrincipal();
        AdminProvisioningService.ProvisionedDirector result = provisioningService.provisionDirector(
                request.nomePrestadora(), request.nomeDiretor(), request.emailDiretor(), actor);
        return new ProvisionResponse(result.prestadoraId(), result.diretorId(), result.conviteUrl(), result.expiraEm());
    }

    public record ProvisionRequest(
            @NotBlank @Size(max = 180) String nomePrestadora,
            @NotBlank @Size(max = 160) String nomeDiretor,
            @NotBlank @Email @Size(max = 254) String emailDiretor) {}

    public record ProvisionResponse(UUID prestadoraId, UUID diretorId, String conviteUrl, OffsetDateTime expiraEm) {}
}
