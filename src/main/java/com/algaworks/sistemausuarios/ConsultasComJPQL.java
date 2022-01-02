package com.algaworks.sistemausuarios;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Configuracao;
import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

public class ConsultasComJPQL {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // try {
        //     Thread.sleep(2000L);    
        // } catch (Exception e) {
        //     //TODO: handle exception
        // }
        
        primeirasConsultas(entityManager);
        escolhendoORetorno(entityManager);
        fazendoProjecoes(entityManager);
        passandoParametros(entityManager);
        fazendoJoins(entityManager);
        fazendoLeftJoin(entityManager);
        carregamentoComJoinFetch(entityManager);
        filtrandoRegistros(entityManager);
        utilizandoOperadoresLogicos(entityManager);
        utilizandoOperadorIn(entityManager);
        ordenandoResultados(entityManager);
        paginandoResultados(entityManager);        

        entityManager.close();
        entityManagerFactory.close();
    }

    public static void primeirasConsultas(EntityManager entityManager) {

        System.out.println("####################");
        System.out.println("Primeiras Consultas");
        System.out.println("####################");
        System.out.println(">>>>>>> Por nome");
        String jpql = "select u from Usuario u order by upper(u.nome)";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + ", " + u.getNome()));

        System.out.println(">>>>>>> Por id");
        String jpqlSing = "select u from Usuario u where u.id = 1";
        TypedQuery<Usuario> typedQuerySing = entityManager.createQuery(jpqlSing, Usuario.class);
        Usuario usuario = typedQuerySing.getSingleResult();
        System.out.println(usuario.getId() + ", " + usuario.getNome());

        System.out.println(">>>>>>> Query Single result para Object");
        String jpqlCast = "select u from Usuario u where u.id = 1";
        Query query = entityManager.createQuery(jpqlCast);
        Usuario usuario2 = (Usuario) query.getSingleResult();
        System.out.println(usuario2.getId() + ", " + usuario2.getNome());
    }

    public static void escolhendoORetorno(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Escolhendo o Retorno");
        System.out.println("####################");
        System.out.println(">> Lendo o domínio");
        String jpql = "select u.dominio from Usuario u where u.id = 1";
        TypedQuery<Dominio> typedQuery = entityManager.createQuery(jpql, Dominio.class);
        Dominio dominio = typedQuery.getSingleResult();
        System.out.println(dominio.getId() + ", " + dominio.getNome());

        jpql = "select u from Usuario u where u.id = 1";
        TypedQuery<Usuario> usuTQ = entityManager.createQuery(jpql, Usuario.class);
        Usuario usu = usuTQ.getSingleResult();
        System.out.println(usu.getDominio().getId() + ", " + usu.getDominio().getNome());

        System.out.println(">> Lendo o nome");
        String jpqlNom = "select u.nome from Usuario u";
        TypedQuery<String> typedQueryNom = entityManager.createQuery(jpqlNom, String.class);
        List<String> listaNom = typedQueryNom.getResultList();
        listaNom.forEach(nome -> System.out.println(nome));
    }

    public static void fazendoProjecoes(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Fazendo projeções");
        System.out.println("####################");
        String jpqlArr = "select id, login, nome from Usuario";
        TypedQuery<Object[]> typedQueryArr = entityManager.createQuery(jpqlArr, Object[].class);
        List<Object[]> listaArr = typedQueryArr.getResultList();
        listaArr.forEach(arr -> System.out.println(String.format("%s | %-10s | %s", arr)));

        //Isto já cria um objeto no resultset.
        String jpqlDto = "select new com.algaworks.sistemausuarios.dto.UsuarioDTO(id, login, nome) " +
                "from Usuario";
        TypedQuery<UsuarioDTO> typedQueryDto = entityManager.createQuery(jpqlDto, UsuarioDTO.class);
        List<UsuarioDTO> listaDto = typedQueryDto.getResultList();
        listaDto.forEach(u -> System.out.println("DTO: " + u.getId() + " | " + u.getNome()));
    }

    public static void passandoParametros(EntityManager entityManager) {

        System.out.println("####################");
        System.out.println("Passando Parâmetros");
        System.out.println("####################");
        String jpql = "select u from Usuario u where u.id = :idUsuario";
        TypedQuery<Usuario> typedQuery = entityManager
                .createQuery(jpql, Usuario.class)
                .setParameter("idUsuario", 1);
        Usuario usuario = typedQuery.getSingleResult();
        System.out.println(usuario.getId() + " - " + usuario.getNome());

        String jpqlLog = "select u from Usuario u where u.login = :loginUsuario";
        TypedQuery<Usuario> typedQueryLog = entityManager
                .createQuery(jpqlLog, Usuario.class)
                .setParameter("loginUsuario", "mit");
        Usuario usuarioLog = typedQueryLog.getSingleResult();
        System.out.println(usuarioLog.getId() + ", " + usuarioLog.getNome());

    }

    public static void fazendoJoins(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Fazendo Joins");
        System.out.println("####################");
        System.out.println(">>>>>>> Somente JOIN");
        String jpql = "select u from Usuario u join u.dominio d where d.id = 1";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + "-" + u.getNome()));

        System.out.println(">>>>>>> LEFT JOIN");
        jpql = "select u from Usuario u left join u.dominio d";
        TypedQuery<Usuario> typedQuery2 = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista2 = typedQuery2.getResultList();
        lista2.forEach(u -> System.out.println(u.getId() + "-" + String.format("%-30s",u.getNome()) + " | Domínio: " + (u.getDominio() == null?"null": u.getDominio().getNome())  ));
    }

    public static void fazendoLeftJoin(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Fazendo Left Joins");
        System.out.println("####################");
        String jpql = "select u, c from Usuario u left join u.configuracao c";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();

        lista.forEach(arr -> {
            // arr[0] == Usuario
            // arr[1] == Configuracao
            String out = String.format("%-30s", ((Usuario) arr[0]).getNome()) +
                         ((arr[1] == null)?"| NULL":"| " + ((Configuracao) arr[1]).getId());
            System.out.println(out);
        });
    }

    public static void carregamentoComJoinFetch(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Fazendo Join Fetch");
        System.out.println("####################");
        System.out.println(">>>>>>> Simples");

        System.out.println(">>>>>>> com Fetch");
        String jpql = "select u from Usuario u join fetch u.configuracao join fetch u.dominio d";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + " | " + u.getNome()));
    }

    public static void filtrandoRegistros(EntityManager entityManager) {
        // LIKE, IS NULL, IS EMPTY, BETWEEN, >, <, >=, <=, =, <>
        // LIKE = select u from Usuario u where u.nome like concat(:nomeUsuario, '%')
        // IS NULL = select u from Usuario u where u.senha is null
        // IS EMPTY = select d from Dominio d where d.usuarios is empty

        System.out.println("####################");
        System.out.println("Filtrando registros");
        System.out.println("####################");
        System.out.println(">>>>>>> LIKE");
        String jpql = "select u from Usuario u join fetch u.dominio d join fetch u.configuracao c where u.nome like :nomeUsuario";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("nomeUsuario", "%lacae%");
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println("Por nome1: " + u.getId() + " | " + u.getNome()));

        System.out.println(">>>>>>> LIKE - concat");
        jpql = "select u from Usuario u join fetch u.dominio d join fetch u.configuracao c where u.nome like concat('%',:nomeUsuario,'%')";
        typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("nomeUsuario", "a");
        typedQuery.getResultList()
                  .forEach(u -> System.out.println("Por nome2: " + u.getId() + " | " + u.getNome()));

        System.out.println(">>>>>>> EMPTY");
        jpql = "select d from Dominio d where d.usuarios is empty";
        entityManager.createQuery(jpql, Dominio.class)
                     .getResultList()
                     .forEach(d -> System.out.println("Domínio sem usuários: " + d.getId()));

        System.out.println(">>>>>>> BETWEEN");
        jpql = "select u from Usuario u join fetch u.dominio d join fetch u.configuracao c where u.ultimoAcesso between :ontem and :hoje";
        entityManager.createQuery(jpql, Usuario.class)
                .setParameter("ontem", LocalDateTime.now().minusDays(1))
                .setParameter("hoje", LocalDateTime.now())
                .getResultList()
                .forEach(u -> System.out.println("Com between: " + u.getId() + " | " + u.getNome()));
    }

    public static void utilizandoOperadoresLogicos(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Operadores Lógicos");
        System.out.println("####################");
        String jpql = "select u from Usuario u left join fetch u.dominio d left join fetch u.configuracao c where " +
                "(u.ultimoAcesso >= :ontem and u.ultimoAcesso <= :hoje )";

        LocalDateTime ontem = LocalDateTime.now().minusDays(1);
        LocalDateTime hoje  = LocalDateTime.now();

        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("ontem", ontem)
                .setParameter("hoje", LocalDateTime.now());
        List<Usuario> lista = typedQuery.getResultList();
        System.out.println("Ontem: " + ontem);
        System.out.println("Hoje : " + hoje);
        lista.forEach(u -> System.out.println(u.getId() + " | " + String.format("%-30s", u.getNome()) + " | ÚltAcesso: " + u.getUltimoAcesso()));
    }

    public static void utilizandoOperadorIn(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Operadores In");
        System.out.println("####################");
        String jpql = "select u from Usuario u where u.id in (:ids)";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class)
                .setParameter("ids", Arrays.asList(1,2));
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(u.getId() + " | " + u.getNome()));
    }

    public static void ordenandoResultados(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Ordenando resultados");
        System.out.println("####################");
        String jpql = "select u from Usuario u " +
                      " left join fetch u.dominio" +  
                      " order by upper(u.dominio.nome), upper(u.nome)";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();
        lista.forEach(u -> System.out.println(String.format("%-15s", u.getDominio()==null?"<vazio>":u.getDominio().getNome()) + " | " + u.getId() + " | " + u.getNome()));
    }

    public static void paginandoResultados(EntityManager entityManager) {
        System.out.println("####################");
        System.out.println("Paginando resultados");
        System.out.println("####################");

        String jpql = "select u from Usuario u";
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(jpql, Usuario.class);
        List<Usuario> lista = typedQuery.getResultList();

        final int LINHAS_POR_PAGINA = 2;

        int paginas = (lista.size() / LINHAS_POR_PAGINA) + (lista.size()%LINHAS_POR_PAGINA==0?0:1);
        System.err.println(String.format("Total de Páginas: %d de %d registros",paginas,lista.size()));


        for (int i = 0; i < paginas; i++) {
            lista = entityManager.createQuery(jpql, Usuario.class)
                 .setFirstResult(i*LINHAS_POR_PAGINA)
                 .setMaxResults(LINHAS_POR_PAGINA)
                 .getResultList();
            System.out.println("------- Página " + (i+1) + " -------");
            lista.forEach(u -> System.out.println(u.getId() + " | " + u.getNome()));
        }

    }

}
