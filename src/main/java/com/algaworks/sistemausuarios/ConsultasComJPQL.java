package com.algaworks.sistemausuarios;

import java.util.List;

import javax.persistence.*;

import com.algaworks.sistemausuarios.dto.UsuarioDTO;
import com.algaworks.sistemausuarios.model.Dominio;
import com.algaworks.sistemausuarios.model.Usuario;

public class ConsultasComJPQL {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("Usuarios-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        primeirasConsultas(entityManager);
        escolhendoORetorno(entityManager);
        fazendoProjecoes(entityManager);
        passandoParametros(entityManager);

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

}
