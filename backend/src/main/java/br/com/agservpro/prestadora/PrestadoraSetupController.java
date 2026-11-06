package br.com.agservpro.prestadora;

import br.com.agservpro.security.AgServProUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prestadoras")
public class PrestadoraSetupController {
    private final PrestadoraSetupService setupService;

    public PrestadoraSetupController(PrestadoraSetupService setupService) {
        this.setupService = setupService;
    }

    @PutMapping("/{prestadoraId}/configuracao")
    public PrestadoraSetupService.SetupResponse completeSetup(
            @PathVariable UUID prestadoraId,
            @Valid @RequestBody SetupRequest request,
            Authentication authentication) {
        AgServProUserDetails director = (AgServProUserDetails) authentication.getPrincipal();
        return setupService.complete(prestadoraId, request.nome(), request.fusoHorario(), director);
    }

    public record SetupRequest(@NotBlank @Size(max = 180) String nome, @NotBlank @Size(max = 64) String fusoHorario) {}
}
