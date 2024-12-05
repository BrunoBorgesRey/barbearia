package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.BarbeadorFilter;
import web.controlevacinacao.model.Barbeador;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.BarbeadorRepository;
import web.controlevacinacao.service.BarbeadorService;

@Controller
@RequestMapping("/barbeadors")
public class BarbeadorController {

    private static final Logger logger = LoggerFactory.getLogger(BarbeadorController.class);

    private BarbeadorService barbeadorService;
    private BarbeadorRepository barbeadorRepository;

     public BarbeadorController(BarbeadorRepository barbeadorRepository, BarbeadorService barbeadorService) {
        this.barbeadorRepository = barbeadorRepository;
        this.barbeadorService = barbeadorService;
    }

    @GetMapping("/nova")
    public String abrirCadastroBarbeador(Barbeador barbeador) {
        return "barbeadors/nova";
    }

    @HxRequest
    @GetMapping("/nova")
    public String abrirCadastroBarbeadorHTMX(Barbeador barbeador) {
        return "barbeadors/nova :: formulario";
    }

    @PostMapping("/nova")
    public String cadastrarBarbeador(Barbeador barbeador) {
        barbeadorService.salvar(barbeador);
        return "redirect:/barbeadors/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirMensagemSucesso(Model model) {
        model.addAttribute("mensagem", "Barbeador cadastrada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/nova")
    public String cadastrarBarbeadorHTMX(@Valid Barbeador barbeador, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A barbeador recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "barbeadors/nova :: formulario";
        } else {
            barbeadorService.salvar(barbeador);
            HtmxLocation hl = new HtmxLocation("/barbeadors/sucesso");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Model model) {
        model.addAttribute("mensagem", "Barbeador cadastrada com sucesso");
        return "mensagem :: texto";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisa() {
        return "barbeadors/pesquisar";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "barbeadors/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(BarbeadorFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Barbeador> pagina = barbeadorRepository.pesquisar(filtro, pageable);
        logger.info("Barbeadors pesquisadas: {}", pagina);
        PageWrapper<Barbeador> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "barbeadors/barbeadors";
    }

    @HxRequest
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(BarbeadorFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Barbeador> pagina = barbeadorRepository.pesquisar(filtro, pageable);
        logger.info("Barbeadors pesquisadas: {}", pagina);
        PageWrapper<Barbeador> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "barbeadors/barbeadors :: tabela";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Barbeador barbeador) {
        return "barbeadors/alterar";
    }

    @HxRequest
    @PostMapping("/abriralterar")
    public String abrirAlterarHTMX(Barbeador barbeador) {
        return "barbeadors/alterar :: formulario";
    }

    @PostMapping("/alterar")
    public String alterar(Barbeador barbeador) {
        barbeadorService.alterar(barbeador);
        return "redirect:/barbeadors/sucesso2";
    }

    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2(Model model) {
        model.addAttribute("mensagem", "Barbeador alterada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Barbeador barbeador, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A barbeador recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "barbeadors/alterar :: formulario";
        } else {
            barbeadorService.alterar(barbeador);
            HtmxLocation hl = new HtmxLocation("/barbeadors/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("mensagem", "Barbeador alterada com sucesso");
        return "mensagem :: texto";
    }

    @PostMapping("/abrirremover")
    public String abrirRemover(Barbeador barbeador) {
        return "barbeadors/confirmarremocao";
    }

    @HxRequest
    @PostMapping("/abrirremover")
    public String abrirRemoverHTMX(Barbeador barbeador) {
        return "barbeadors/confirmarremocao :: confirmacao";
    }

    @PostMapping("/remover")
    public String remover(Barbeador barbeador) {
        barbeador.setStatus(Status.INATIVO);
        barbeadorService.alterar(barbeador);
        return "redirect:/barbeadors/sucesso3";
    }

    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3(Model model) {
        model.addAttribute("mensagem", "Barbeador removida com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/remover")
    public String removerHTMX(Barbeador barbeador, HtmxResponse.Builder htmxResponse) {
        barbeador.setStatus(Status.INATIVO);
        barbeadorService.alterar(barbeador);
        HtmxLocation hl = new HtmxLocation("/barbeadors/sucesso3");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
        return "mensagem";
    }

    @HxRequest
    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3HTMX(Model model) {
        model.addAttribute("mensagem", "Barbeador removida com sucesso");
        return "mensagem :: texto";
    }
}
