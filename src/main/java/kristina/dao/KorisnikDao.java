/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kristina.dao;


import kristina.data.Korisnik;
import kristina.exception.prodavnica_exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KorisnikDao {

    private static final KorisnikDao instance = new KorisnikDao();

    private KorisnikDao() {}

    public static KorisnikDao getInstance() {
        return instance;
    }

    public List<Korisnik> getAllKorisnici() throws prodavnica_exception {
        List<Korisnik> korisnici = new ArrayList<>();
        String sql = "SELECT * FROM Korisnik";  
        try (Connection con = ResourcesManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Korisnik k = new Korisnik();
                k.setKorisnik_Id(rs.getInt("korisnik_id"));
                k.setImeIPrezime(rs.getString("ime_i_prezime"));
                k.setUsername(rs.getString("username"));
                k.setPassword(rs.getString("password"));
                k.setEmail(rs.getString("e_mail"));
                k.setDatumRodjenja(rs.getString("datum_rodjenja"));
                k.setStanjeRacuna(rs.getInt("stanje_racuna"));
                k.setKolicinaPotrosenogNovca(rs.getInt("kolicina_potrosenog_novca"));
                korisnici.add(k);
            }
        } catch (SQLException ex) {
            throw new prodavnica_exception("Error while fetching korisnici", ex);
        }
        return korisnici;
    }

  
    public void registracija(Korisnik k, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

           
            ps = con.prepareStatement("INSERT INTO Korisnik(ime_i_prezime, username, password, e_mail, datum_rodjenja,stanje_racuna,kolicina_potrosenog_novca) VALUES(?,?,?,?,?,?,?)");
            
            ps.setString(1, k.getImeIPrezime());
            ps.setString(2, k.getUsername());
            ps.setString(3, k.getPassword());
            ps.setString(4, k.getEmail());
            ps.setString(5, k.getDatumRodjenja());
            ps.setInt(6, k.getStanjeRacuna());
            ps.setInt(7, k.getKolicinaPotrosenogNovca());
            
            ps.executeUpdate();

        } finally {
            ResourcesManager.closeResources(rs, ps);
        }
    }
    
    
    
    public Korisnik find(String username, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Korisnik k = null;
        try {
            ps = con.prepareStatement("SELECT * FROM Korisnik where username=?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
               
                k = new Korisnik(rs.getInt("Korisnik_id"),rs.getString("ime_i_prezime"), username,rs.getString("password"), rs.getString("e_mail"),rs.getString("datum_rodjenja"), rs.getInt("stanje_racuna"), rs.getInt("kolicina_potrosenog_novca"));
            }
        } finally {
            ResourcesManager.closeResources(rs, ps);
        }
        return k;
    }
   
}

