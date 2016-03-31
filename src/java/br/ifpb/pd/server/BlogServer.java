/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd.server;

import br.ifpb.pd.model.Noticia;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 */
@Path("blog")
public class BlogServer {

    @Context
    private UriInfo context;
    
    private EntityManager em;

    public BlogServer() {
        em = Persistence.createEntityManagerFactory("BlogPU").createEntityManager();
    }
    
    @POST
    @Consumes("application/xml")
    public Response criarNoticia(Noticia noticia) {
        Noticia n = em.find(Noticia.class, noticia.getId());
        if (n == null) {
            em.getTransaction().begin();
            em.persist(noticia);
            em.getTransaction().commit();
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    
    @PUT
    @Path("{id}/{titulo}")
    @Produces("application/json")
    public Response atualizarNoticia(@PathParam("id") Long id, @PathParam("titulo") String titulo) {
        Noticia n = em.find(Noticia.class, id);
        if (n != null) {
            n.setTitulo(titulo);
            em.getTransaction().begin();
            em.merge(n);
            em.getTransaction().commit();
            return Response.ok(n, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @DELETE
    @Path("{id}")
    @Produces("application/xml")
    public Response removerNoticia(@PathParam("id") Long id) {
        Noticia n = em.find(Noticia.class, id);
        if (n!= null) {
            em.getTransaction().begin();
            em.remove(n);
            em.getTransaction().commit();
            return Response.ok(n, MediaType.APPLICATION_XML).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Produces("text/html")
    public Response getNoticia(@QueryParam("id") Long id) {
        Noticia n = em.find(Noticia.class, id);
        if (n != null) {
            String html = "<html><body><table border='1' align='center'>"
                    + "<tr>"
                    + " <th>id</th>"
                    + " <td>"+n.getId()+"</td>"
                    + "</tr>"
                    + "<tr>"
                    + " <th>titulo</th>"
                    + " <td>"+n.getTitulo()+"</td>"
                    + "</tr>"
                    + "<tr>"
                    + " <th>autor</th>"
                    + " <td>"+n.getAutor()+"</td>"
                    + "</tr>"
                    + "<tr>"
                    + " <th>conteudo</th>"
                    + " <td>"+n.getConteudo()+"</td>"
                    + "</tr>"
                    + "<tr>"
                    + " <th>data</th>"
                    + " <td>"+n.getData()+"</td>"
                    + "</tr>"
                    +"</table></body></html>";
            return Response.ok(html, MediaType.TEXT_HTML).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
