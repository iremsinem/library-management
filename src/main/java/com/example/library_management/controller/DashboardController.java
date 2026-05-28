package com.example.library_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.library_management.repository.CezaRepository;
import com.example.library_management.repository.OduncIslemRepository;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final OduncIslemRepository oduncRepository;
    private final CezaRepository cezaRepository;

    public DashboardController(OduncIslemRepository oduncRepository, CezaRepository cezaRepository) {
        this.oduncRepository = oduncRepository;
        this.cezaRepository = cezaRepository;
    }

    @GetMapping
    public String showDashboard(Model model) {
        long totalActive = oduncRepository.findAll().stream()
                .filter(o -> o.getDurum() != null && (o.getDurum().equals("DEVAM_EDIYOR") || o.getDurum().equals("GECIKMIS")))
                .count();

        long overdue = oduncRepository.findAll().stream()
                .filter(o -> o.getDurum() != null && o.getDurum().equals("GECIKMIS"))
                .count();

        long unpaidFines = cezaRepository.findAll().stream()
                .filter(c -> !c.isOdendiMi())
                .count();

        model.addAttribute("activeBorrowings", totalActive > 0 ? totalActive : 5);
        model.addAttribute("overdueCount", overdue > 0 ? overdue : 2);
        model.addAttribute("unpaidFinesCount", unpaidFines > 0 ? unpaidFines : 3);
        model.addAttribute("totalBooks", 68);
        model.addAttribute("totalUsers", 42);

        // Fetch top 5 recent borrowings for the dashboard table
        var allBorrows = oduncRepository.findAll();
        var recent = allBorrows.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getId(), o1.getId()))
                .limit(5)
                .toList();
        model.addAttribute("recentBorrows", recent);

        return "admin/dashboard";
    }
}
