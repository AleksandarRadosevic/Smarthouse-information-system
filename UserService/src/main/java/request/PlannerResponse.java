/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request;

import entiteti.Planner;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Aleksandar
 */
public class PlannerResponse implements Serializable{
    List<Planner>obligations;
    private Integer userId;

    public PlannerResponse(List<Planner> obligations, Integer userId) {
        this.obligations = obligations;
        this.userId = userId;
    }

    public List<Planner> getObligations() {
        return obligations;
    }

    public void setObligations(List<Planner> obligations) {
        this.obligations = obligations;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public void printObligations(){
        for (Planner p:obligations)
            System.out.println(p.getObligation()+"\t"+p.getStart()+"\t"+p.getDestination()+"\t"+p.getDuration());
    }
    public StringBuilder getObligationsBuilder(){
        StringBuilder b=new StringBuilder("Obaveze:\n");
        for (Planner p:obligations){
            b.append(p.getId()+".\t"+p.getObligation()+"\t"+p.getStart()+"\t"+p.getDestination()+"\t"+p.getDuration()+"\n");
        }
        return b;
    }
}
