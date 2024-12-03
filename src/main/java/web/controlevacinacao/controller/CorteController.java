package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.CorteFilter;
import web.controlevacinacao.model.Corte;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.CorteRepository;
import web.controlevacinacao.service.CorteService;

@Controller
@RequestMapping("/cortes")
public class CorteController {
    private static final Logger logger = LoggerFactory.getLogger(CorteController.class);

    private CorteService corteService;
    private CorteRepository corteRepository;

     public CorteController(CorteRepository corteRepository, CorteService corteService) {
        this.corteRepository = corteRepository;
        this.corteService = corteService;
    }

    @GetMapping("/nova")
    public String abrirCadastroCorte(Corte corte) {
        return "cortes/nova";
    }

    @HxRequest
    @GetMapping("/nova")
    public String abrirCadastroCorteHTMX(Corte corte) {
        return "cortes/nova :: formulario";
    }

    @PostMapping("/nova")
    public String cadastrarCorte(Corte corte) {
        corteService.salvar(corte);
        return "redirect:/cortes/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirMensagemSucesso(Model model) {
        model.addAttribute("mensagem", "Corte cadastrada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/nova")
    public String cadastrarCorteHTMX(@Valid Corte corte, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A corte recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "cortes/nova :: formulario";
        } else {
            corteService.salvar(corte);
            HtmxLocation hl = new HtmxLocation("/cortes/sucesso");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Model model) {
        model.addAttribute("mensagem", "Corte cadastrada com sucesso");
        return "mensagem :: texto";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisa() {
        return "cortes/pesquisar";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "cortes/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(CorteFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Corte> pagina = corteRepository.pesquisar(filtro, pageable);
        logger.info("Cortes pesquisadas: {}", pagina);
        PageWrapper<Corte> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "cortes/cortes";
    }

    @HxRequest
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(CorteFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Corte> pagina = corteRepository.pesquisar(filtro, pageable);
        logger.info("Cortes pesquisadas: {}", pagina);
        PageWrapper<Corte> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "cortes/cortes :: tabela";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Corte corte) {
        return "cortes/alterar";
    }

    @HxRequest
    @PostMapping("/abriralterar")
    public String abrirAlterarHTMX(Corte corte) {
        return "cortes/alterar :: formulario";
    }

    @PostMapping("/alterar")
    public String alterar(Corte corte) {
        corteService.alterar(corte);
        return "redirect:/cortes/sucesso2";
    }

    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2(Model model) {
        model.addAttribute("mensagem", "Corte alterada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Corte corte, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A corte recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "cortes/alterar :: formulario";
        } else {
            corteService.alterar(corte);
            HtmxLocation hl = new HtmxLocation("/cortes/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("mensagem", "Corte alterada com sucesso");
        return "mensagem :: texto";
    }

    @PostMapping("/abrirremover")
    public String abrirRemover(Corte corte) {
        return "cortes/confirmarremocao";
    }

    @HxRequest
    @PostMapping("/abrirremover")
    public String abrirRemoverHTMX(Corte corte) {
        return "cortes/confirmarremocao :: confirmacao";
    }

    @PostMapping("/remover")
    public String remover(Corte corte) {
        corte.setStatus(Status.INATIVO);
        corteService.alterar(corte);
        return "redirect:/cortes/sucesso3";
    }

    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3(Model model) {
        model.addAttribute("mensagem", "Corte removida com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/remover")
    public String removerHTMX(Corte corte, HtmxResponse.Builder htmxResponse) {
        corte.setStatus(Status.INATIVO);
        corteService.alterar(corte);
        HtmxLocation hl = new HtmxLocation("/cortes/sucesso3");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
        return "mensagem";
    }

    @HxRequest
    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3HTMX(Model model) {
        model.addAttribute("mensagem", "Corte removida com sucesso");
        return "mensagem :: texto";
    }
}
