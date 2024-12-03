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
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ClienteRepository;
import web.controlevacinacao.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    public ClienteController(ClienteRepository clienteRepository, ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }

    // @GetMapping("/todas")
    // public String mostrarTodasClientes(Model model) {
    // List<Cliente> clientes = clienteRepository.findAll();
    // logger.info("Clientes buscadas: {}", clientes);
    // model.addAttribute("clientes", clientes);
    // return "clientes/todas";
    // }

    @GetMapping("/nova")
    public String abrirCadastroCliente(Cliente cliente) {
        return "clientes/nova";
    }

    @HxRequest
    @GetMapping("/nova")
    public String abrirCadastroClienteHTMX(Cliente cliente) {
        return "clientes/nova :: formulario";
    }

    @PostMapping("/nova")
    public String cadastrarCliente(Cliente cliente) {
        clienteService.salvar(cliente);
        return "redirect:/clientes/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirMensagemSucesso(Model model) {
        model.addAttribute("mensagem", "Cliente cadastrada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/nova")
    public String cadastrarClienteHTMX(@Valid Cliente cliente, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A cliente recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "clientes/nova :: formulario";
        } else {
            clienteService.salvar(cliente);
            HtmxLocation hl = new HtmxLocation("/clientes/sucesso");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Cliente cliente, Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Cliente cadastrada com sucesso!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "clientes/nova :: formulario";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisa() {
        return "clientes/pesquisar";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "clientes/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(ClienteFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Cliente> pagina = clienteRepository.pesquisar(filtro, pageable);
        logger.info("Clientes pesquisadas: {}", pagina);
        PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "clientes/clientes";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(ClienteFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Cliente> pagina = clienteRepository.pesquisar(filtro, pageable);
        logger.info("Clientes pesquisadas: {}", pagina);
        PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "clientes/clientes :: tabela";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Cliente cliente) {
        return "clientes/alterar";
    }

    @HxRequest
    @PostMapping("/abriralterar")
    public String abrirAlterarHTMX(Cliente cliente) {
        return "clientes/alterar :: formulario";
    }

    @PostMapping("/alterar")
    public String alterar(Cliente cliente) {
        clienteService.alterar(cliente);
        return "redirect:/clientes/sucesso2";
    }

    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2(Model model) {
        model.addAttribute("mensagem", "Cliente alterada com sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Cliente cliente, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("A cliente recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "clientes/alterar :: formulario";
        } else {
            clienteService.alterar(cliente);
            HtmxLocation hl = new HtmxLocation("/clientes/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Cliente alterada com sucesso!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "clientes/pesquisar :: formulario";
    }

    @PostMapping("/remover")
    public String remover(Cliente cliente) {
        cliente.setStatus(Status.INATIVO);
        clienteService.alterar(cliente);
        return "redirect:/clientes/sucesso3";
    }

    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3(Model model) {
        model.addAttribute("mensagem", "Cliente removida com sucesso");
        return "mensagem";
    }

    @HxRequest
    @HxLocation(path = "/clientes/sucesso3", target = "#main", swap = "outerHTML")
    @PostMapping("/remover")
    public String removerHTMX(Cliente cliente) {
        cliente.setStatus(Status.INATIVO);
        clienteService.alterar(cliente);
        return "mensagem";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Cliente removida com sucesso!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "clientes/pesquisar :: formulario";
    }

}
