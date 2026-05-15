package com.example.library_management.controller;

import com.example.library_management.model.Ceza;
import com.example.library_management.repository.CezaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cezalar") // Tarayıcıda localhost:8080/cezalar adresini kullanır
public class CezaController {

    // Hocanın sayfa 32'de yaptığı gibi Repository'yi constructor üzerinden bağlıyoruz
    private final CezaRepository cezaRepository;

    public CezaController(CezaRepository cezaRepository) {
        this.cezaRepository = cezaRepository;
    }

    // 1. Listeleme: Hocanın listStudents() metoduna benzer yapı
    @GetMapping
    public String listCezalar(Model model) {
        // Veritabanındaki tüm cezaları listele
        List<Ceza> cezalar = cezaRepository.findAll();
        model.addAttribute("cezalar", cezalar);

        // Yeni ceza girişi yapılabilecek boş bir nesne gönder (Form için)
        model.addAttribute("yeniCeza", new Ceza());

        return "admin/ceza"; // "ceza-list" yerine yeni yol
    }

    // 2. Kaydetme: Hocanın saveStudent() metoduna benzer yapı
    @PostMapping("/save")
    public String saveCeza(@ModelAttribute("yeniCeza") Ceza ceza) {
        // Formdan gelen ceza bilgilerini veritabanına kaydeder
        cezaRepository.save(ceza);

        // Kayıt sonrası listeye geri döndürür (Redirect)
        return "redirect:/cezalar";
    }

    // 3. Silme: Hocanın deleteStudent() metoduna benzer yapı
    @GetMapping("/delete/{id}")
    public String deleteCeza(@PathVariable int id) {
        // ID'si verilen cezayı siler
        cezaRepository.deleteById(id);

        // Silme sonrası listeye geri döndürür
        return "redirect:/cezalar";
    }

    // CezaController.java içine eklenecek metod
    @GetMapping("/odeme/{id}")
    public String cezaTahsilEt(@PathVariable int id) {
        // 1. Veritabanından o ID'ye sahip cezayı bul (Hocanın repository mantığı)
        Ceza bulunanCeza = cezaRepository.findById(id).orElse(null);

        if (bulunanCeza != null) {
            // 2. Cezayı "Ödendi" olarak işaretle
            bulunanCeza.setOdendiMi(true);

            // 3. Güncellenmiş halini veritabanına geri kaydet
            cezaRepository.save(bulunanCeza);
        }

        // 4. İşlem bitince listeyi yenilemek için cezalar sayfasına geri dön
        return "redirect:/cezalar";
    }


    @GetMapping("/tahsil-et/{id}")
    public String tahsilEt(@PathVariable int id) {
        // 1. Cezayı ID ile bul
        Ceza ceza = cezaRepository.findById(id).get();

        // 2. Ödeme durumunu güncelle
        ceza.setOdendiMi(true);

        // 3. Güncellenmiş cezayı kaydet
        cezaRepository.save(ceza);

        // 4. Listeye geri dön
        return "redirect:/cezalar";
    }

    // Kullanıcıya özel ceza listeleme sayfası
    @GetMapping("/user")
    public String listUserCezalar(Model model) {
        List<Ceza> cezalar = cezaRepository.findAll();
        model.addAttribute("cezalar", cezalar);
        return "user/ceza";
    }
}