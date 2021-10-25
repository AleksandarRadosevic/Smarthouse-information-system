/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.userservice.resources;

import entiteti.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author Aleksandar
 */
@Path("login")
@Stateless
public class LoginResource {

    @PersistenceContext
    EntityManager em;
    
    @GET
    public Response login(@Context HttpHeaders httpHeaders,@QueryParam("username") String username,
            @QueryParam("password") String password){
        
        Query query=em.createQuery("Select u from User u WHERE u.username=:username AND u.password=:password",User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<User>users=query.getResultList();
        if (users.size()==0)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else return Response.ok().entity("success").build();
    }

}
