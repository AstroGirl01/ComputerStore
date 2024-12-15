/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kristina.dao;

import kristina.data.Proizvod;
import kristina.exception.prodavnica_exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProizvodDao {

    private static final ProizvodDao instance = new ProizvodDao();

    private ProizvodDao() {}

    public static ProizvodDao getInstance() {
        return instance;
    }

    public List<Proizvod> getAllProizvodi() throws prodavnica_exception {
        List<Proizvod> Proizvodi = new ArrayList<>();
        String sql = "SELECT * FROM Proizvod";  
        try (Connection con = ResourcesManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Proizvod p = new Proizvod();
                p.setProizvod_id(rs.getInt("Proizvod_id"));
                p.setNaziv(rs.getString("naziv"));
                p.setCena(rs.getInt("cena"));
                p.setVrsta_opreme(rs.getString("vrsta_opreme"));
                p.setStanje_na_lageru(rs.getInt("stanje_na_lageru"));
                
                Proizvodi.add(p);
            }
        } catch (SQLException ex) {
            throw new prodavnica_exception("Error while fetching Proizvodi", ex);
        }
        return Proizvodi;
    }
    
    public Proizvod find(String naziv, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Proizvod p = null;
        try {
            ps = con.prepareStatement("SELECT * FROM Proizvod where naziv=?");
            ps.setString(1, naziv);
            rs = ps.executeQuery();
            if (rs.next()) {
               
                p = new Proizvod(rs.getInt("Proizvod_id"), naziv, rs.getInt("cena"),rs.getString("vrsta_opreme"), rs.getInt("stanje_na_lageru"));
            }
        } finally {
            ResourcesManager.closeResources(rs, ps);
        }
        return p;
    }
    
    
    
    
}