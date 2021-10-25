/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userdevice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Aleksandar
 */
public class UserDevice {

    private String username;
    private String password;
    private Boolean isLogged = false;
    private static Scanner scanner = new Scanner(System.in);

    public void login() {
        while (!isLogged) {
            try {
                System.out.println("Unesite username:");

                username = scanner.nextLine();
                System.out.println("Unesite password:");
                password = scanner.nextLine();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://localhost:8080/UserService/test/login?username="
                        + username + "&password=" + password).build();

                Response response = client.newCall(request).execute();
                if (response.body().string().equals("success")) {
                    System.out.println("Uspesno ste ulogovani!");
                    isLogged = true;
                } else {
                    System.out.println("Neuspesno logovanje");
                }
            } catch (IOException ex) {
                Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void playSong() {
        try {
            scanner.nextLine();
            System.out.println("Unesite naziv pesme");
            String song = scanner.nextLine();
            OkHttpClient client = new OkHttpClient();
            String credentials = Credentials.basic(username, password);
            Request request = new Request.Builder().url("http://localhost:8080/UserService/test/device?song=" + song).header("Authorization", credentials).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
            }
        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPlaylist() {
        try {
            OkHttpClient client = new OkHttpClient();
            String credentials = Credentials.basic(username, password);
            Request request = new Request.Builder().url("http://localhost:8080/UserService/test/device/playlist").header("Authorization", credentials).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
                return;
            }
            request = new Request.Builder().url("http://localhost:8080/UserService/test/device/result").header("Authorization", credentials).build();
            response = client.newCall(request).execute();
            String str = response.body().string();
            System.out.println(str);

        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setAlarm() {
        try {
            scanner.nextLine();
            System.out.println("Unesite vreme pocetka alarma:");
            String start = scanner.nextLine();
            System.out.println("Unesite ime pesme");
            String song = scanner.nextLine();
            System.out.println("Unesite broj ponavljanja alarma");
            String repetition = scanner.nextLine();
            String status = "1";

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("start", start)
                    .add("song", "" + song)
                    .add("repetition", repetition)
                    .add("status", "" + status)
                    .build();
            String credentials = Credentials.basic(username, password);

            Request request = new Request.Builder()
                    .url("http://localhost:8080/UserService/test/alarm?start=" + start
                            + "&song=" + song + "&repetition=" + repetition + "&status=" + status).post(formBody).header("Authorization", credentials)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
                return;
            }
            System.out.println("Alarm uspesno postavljen");
        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addObligation() {
        try {
            scanner.nextLine();
            System.out.println("Unesite vreme pocetka obaveze:");
            String startTime = scanner.nextLine();
            System.out.println("Unesite trajanje obaveze");
            String duration = scanner.nextLine();
            System.out.println("Unesite mesto obaveze");
            String destination = scanner.nextLine();
            System.out.println("Unesite opis obaveze");
            String obligation = scanner.nextLine();
            System.out.println("Za postavljanje alarma unesite 1 u suprotnom 0");
            String alarm = scanner.nextLine();

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("startTime", startTime)
                    .add("duration", "" + duration)
                    .add("destination", destination)
                    .add("obligation", "" + obligation)
                    .add("alarm", alarm)
                    .build();
            String credentials = Credentials.basic(username, password);

            Request request = new Request.Builder()
                    .url("http://localhost:8080/UserService/test/planer/insert?startTime=" + startTime
                            + "&duration=" + duration + "&destination=" + destination + "&obligation=" + obligation + "&alarm=" + alarm).post(formBody).header("Authorization", credentials)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
                return;
            }

        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteObligation() {
        try {
            scanner.nextLine();
            System.out.println("Unesite broj obaveze");
            String id = scanner.nextLine();

            OkHttpClient client = new OkHttpClient();

            String credentials = Credentials.basic(username, password);

            Request request = new Request.Builder()
                    .url("http://localhost:8080/UserService/test/planer/delete?id=" + id).delete().header("Authorization", credentials)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
                return;
            }
            System.out.println("Uspesno brisanje");
        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getObligation() {
        try {
            OkHttpClient client = new OkHttpClient();
            String credentials = Credentials.basic(username, password);
            Request request = new Request.Builder().url("http://localhost:8080/UserService/test/planer/select").header("Authorization", credentials).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
                return;
            }
            request = new Request.Builder().url("http://localhost:8080/UserService/test/planer/result").header("Authorization", credentials).build();
            response = client.newCall(request).execute();
            String str = response.body().string();
            System.out.println(str);

        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateObligation() {
        try {
            scanner.nextLine();
            System.out.println("Unesite broj obaveze");
            String id = scanner.nextLine();
            System.out.println("Unesite vreme pocetka obaveze:");
            String startTime = scanner.nextLine();
            System.out.println("Unesite trajanje obaveze");
            String duration = scanner.nextLine();
            System.out.println("Unesite mesto obaveze");
            String destination = scanner.nextLine();
            System.out.println("Unesite opis obaveze");
            String obligation = scanner.nextLine();
            System.out.println("Za postavljanje alarma unesite 1 u suprotnom 0");
            String alarm = scanner.nextLine();

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("id", "" + id)
                    .add("startTime", startTime)
                    .add("duration", "" + duration)
                    .add("destination", destination)
                    .add("obligation", "" + obligation)
                    .add("alarm", "" + Integer.parseInt(alarm))
                    .build();
            String credentials = Credentials.basic(username, password);

            Request request = new Request.Builder()
                    .url("http://localhost:8080/UserService/test/planer/update?id=" + id + "&startTime=" + startTime
                            + "&duration=" + duration + "&destination=" + destination + "&obligation=" + obligation + "&alarm=" + alarm).post(formBody).header("Authorization", credentials)
                    .build();
            System.out.println("http://localhost:8080/UserService/test/planer/update?id=" + id + "&startTime=" + startTime
                    + "&duration=" + duration + "&destination=" + destination + "&obligation=" + obligation + "&alarm=" + alarm);
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Doslo je do greske!");
                return;
            }

        } catch (IOException ex) {
            Logger.getLogger(UserDevice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void menu() {
        System.out.println("1.Pusti pesmu");
        System.out.println("2.Dohvati plejlistu");
        System.out.println("3.Postavi alarm");
        System.out.println("4.Prikazi sve obaveze");
        System.out.println("5.Dodaj obavezu");
        System.out.println("6.Izmeni obavezu");
        System.out.println("7.Obrisi obavezu");
        System.out.println("8.Kraj rada");

    }

    public static void main(String[] args) {
        UserDevice userDevice = new UserDevice();
        userDevice.login();
        System.out.println("Izaberite neku od opcija");
        int val = 0;
        while (true) {
            menu();
            System.out.println("Unesite neki broj");
            val = scanner.nextInt();
            switch (val) {
                case 1:
                    userDevice.playSong();
                    break;
                case 2:
                    userDevice.getPlaylist();
                    break;
                case 3:
                    userDevice.setAlarm();
                    break;
                case 4:
                    userDevice.getObligation();
                    break;
                case 5:
                    userDevice.addObligation();
                    break;
                case 6:
                    userDevice.updateObligation();
                    break;
                case 7:
                    userDevice.deleteObligation();
                    break;
                case 8:
                    System.out.println("---------------------------KRAJ----------------------------------------");
                    return;
            }
        }

    }

}
