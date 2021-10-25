/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deviceRequest;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Aleksandar
 */
public class DeviceSendRequest implements Serializable{

    private List<String> results;
    private Integer userId;

    public DeviceSendRequest(Integer userId, List<String> results) {
        this.results = results;
        this.userId = userId;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
