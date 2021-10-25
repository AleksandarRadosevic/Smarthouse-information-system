/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeCalculation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Aleksandar
 */
public class timeCalc {
    
    public static boolean isEqual(LocalDateTime first){
    Date date=new Date();
    LocalDateTime second = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    if (second.getYear()==first.getYear() && second.getMonth()==first.getMonth() && second.getDayOfMonth()==first.getDayOfMonth()
            && second.getHour()==first.getHour() && second.getMinute()==first.getMinute())
        return true;
    
    return false;
    }
    
    
    
}
