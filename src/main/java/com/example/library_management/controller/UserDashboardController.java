package com.example.library_management.controller;

import com.example.library_management.model.Kitaplar;
import com.example.library_management.model.Kullanici;
import com.example.library_management.model.OduncIslemi;
import com.example.library_management.model.Rezervasyonlar;
import com.example.library_management.repository.CezaRepository;
import com.example.library_management.repository.KitaplarRepository;
import com.example.library_management.repository.OduncIslemRepository;
import com.example.library_management.repository.RezervasyonlarRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

@Controller
@RequestMapping("/user/dashboard")
public class UserDashboardController {

    private final OduncIslemRepository oduncRepository;
    private final CezaRepository cezaRepository;
    private final RezervasyonlarRepository rezervasyonlarRepository;
    private final KitaplarRepository kitaplarRepository;

    public UserDashboardController(OduncIslemRepository oduncRepository,
                                   CezaRepository cezaRepository,
                                   RezervasyonlarRepository rezervasyonlarRepository,
                                   KitaplarRepository kitaplarRepository) {
        this.oduncRepository = oduncRepository;
        this.cezaRepository = cezaRepository;
        this.rezervasyonlarRepository = rezervasyonlarRepository;
        this.kitaplarRepository = kitaplarRepository;
    }

    @GetMapping
    public String showUserDashboard(HttpSession session, Model model) {

        Kullanici aktifKullanici = (Kullanici) session.getAttribute("aktifKullanici");

        if (aktifKullanici == null) {
            return "redirect:/login";
        }

        int kullaniciId = aktifKullanici.getId();

        List<OduncIslemi> aktifOduncler = oduncRepository.findAll().stream()
                .filter(o -> o.getKullaniciId() == kullaniciId)
                .filter(o -> o.getDurum() != null &&
                        (o.getDurum().equals("DEVAM_EDIYOR") || o.getDurum().equals("GECIKMIS")))
                .toList();

        List<OduncIslemi> okunanKitaplar = oduncRepository.findAll().stream()
                .filter(o -> o.getKullaniciId() == kullaniciId)
                .filter(o -> o.getDurum() != null && o.getDurum().equals("TESLIM_EDILDI"))
                .toList();

        List<Map<String, Object>> currentBooks = new ArrayList<>();

        for (OduncIslemi odunc : aktifOduncler) {
            Map<String, Object> item = new HashMap<>();

            Kitaplar kitap = kitaplarRepository.findById(odunc.getKopyaId()).orElse(null);

            item.put("baslik", kitap != null ? kitap.getBaslik() : "Bilinmeyen Kitap");
            item.put("yazar", kitap != null && kitap.getYazar() != null
                    ? kitap.getYazar().getAd() + " " + kitap.getYazar().getSoyad()
                    : "-");
            item.put("teslimTarihi", odunc.getTeslimTarihi());
            item.put("durum", odunc.getDurum());

            currentBooks.add(item);
        }

        List<Rezervasyonlar> aktifRezervasyonlar = rezervasyonlarRepository.findByKullaniciId(kullaniciId).stream()
                .filter(r -> r.getDurum() != null &&
                        (r.getDurum().equals("BEKLEMEDE") ||
                                r.getDurum().equals("ONAYLANDI") ||
                                r.getDurum().equals("HAZIR")))
                .toList();

        double unpaidSum = cezaRepository.findAll().stream()
                .filter(c -> c.getKullaniciId() == kullaniciId)
                .filter(c -> !c.isOdendiMi())
                .mapToDouble(c -> c.getCezaMiktari() != null ? c.getCezaMiktari().doubleValue() : 0.0)
                .sum();

        model.addAttribute("kullanici", aktifKullanici);
        model.addAttribute("myActiveCount", aktifOduncler.size());
        model.addAttribute("myReadCount", okunanKitaplar.size());
        model.addAttribute("myReservationsCount", aktifRezervasyonlar.size());
        model.addAttribute("myFinesAmount", String.format("%.2f", unpaidSum) + " ₺");

        model.addAttribute("currentBooks", currentBooks);
        model.addAttribute("activeReservations", aktifRezervasyonlar);

        return "user/dashboard";
    }
}