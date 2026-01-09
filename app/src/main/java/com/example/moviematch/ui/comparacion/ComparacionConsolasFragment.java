package com.example.moviematch.ui.comparacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ComparacionConsolasFragment extends Fragment {

    private static class ConsoleInfo {
        private final String name;
        private final double priceUsd;
        private final double powerTflops;
        private final int exclusivesScore;
        private final int servicesScore;

        ConsoleInfo(String name, double priceUsd, double powerTflops, int exclusivesScore, int servicesScore) {
            this.name = name;
            this.priceUsd = priceUsd;
            this.powerTflops = powerTflops;
            this.exclusivesScore = exclusivesScore;
            this.servicesScore = servicesScore;
        }
    }

    private ConsoleInfo selectedOne;
    private ConsoleInfo selectedTwo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_console_comparison, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerOne = view.findViewById(R.id.spinnerConsoleOne);
        Spinner spinnerTwo = view.findViewById(R.id.spinnerConsoleTwo);
        LinearLayout resultsCard = view.findViewById(R.id.cardComparisonResults);
        TextView txtComparisonSubtitle = view.findViewById(R.id.txtComparisonSubtitle);
        TextView txtPriceComparison = view.findViewById(R.id.txtPriceComparison);
        TextView txtPowerComparison = view.findViewById(R.id.txtPowerComparison);
        TextView txtExclusiveComparison = view.findViewById(R.id.txtExclusiveComparison);
        TextView txtServiceComparison = view.findViewById(R.id.txtServiceComparison);
        TextView txtConsoleOneStrengths = view.findViewById(R.id.txtConsoleOneStrengths);
        TextView txtConsoleTwoStrengths = view.findViewById(R.id.txtConsoleTwoStrengths);

        List<ConsoleInfo> consoles = obtenerConsolas();
        List<String> consoleNames = new ArrayList<>();
        String prompt = getString(R.string.console_select_prompt);
        consoleNames.add(prompt);
        for (ConsoleInfo console : consoles) {
            consoleNames.add(console.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, consoleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOne.setAdapter(adapter);
        spinnerTwo.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConsoleInfo selected = position > 0 ? consoles.get(position - 1) : null;
                if (parent.getId() == R.id.spinnerConsoleOne) {
                    selectedOne = selected;
                } else if (parent.getId() == R.id.spinnerConsoleTwo) {
                    selectedTwo = selected;
                }
                actualizarComparacion(resultsCard, txtComparisonSubtitle, txtPriceComparison, txtPowerComparison,
                        txtExclusiveComparison, txtServiceComparison, txtConsoleOneStrengths, txtConsoleTwoStrengths);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (parent.getId() == R.id.spinnerConsoleOne) {
                    selectedOne = null;
                } else if (parent.getId() == R.id.spinnerConsoleTwo) {
                    selectedTwo = null;
                }
            }
        };

        spinnerOne.setOnItemSelectedListener(listener);
        spinnerTwo.setOnItemSelectedListener(listener);
    }

    private void actualizarComparacion(LinearLayout resultsCard,
                                       TextView txtComparisonSubtitle,
                                       TextView txtPriceComparison,
                                       TextView txtPowerComparison,
                                       TextView txtExclusiveComparison,
                                       TextView txtServiceComparison,
                                       TextView txtConsoleOneStrengths,
                                       TextView txtConsoleTwoStrengths) {
        if (selectedOne == null || selectedTwo == null) {
            resultsCard.setVisibility(View.GONE);
            return;
        }

        resultsCard.setVisibility(View.VISIBLE);

        if (selectedOne.name.equals(selectedTwo.name)) {
            txtComparisonSubtitle.setText(getString(R.string.console_comparison_same_warning));
            txtPriceComparison.setText(getString(R.string.console_comparison_price_placeholder));
            txtPowerComparison.setText(getString(R.string.console_comparison_power_placeholder));
            txtExclusiveComparison.setText(getString(R.string.console_comparison_exclusives_placeholder));
            txtServiceComparison.setText(getString(R.string.console_comparison_services_placeholder));
            txtConsoleOneStrengths.setText(getString(R.string.console_comparison_strengths_placeholder));
            txtConsoleTwoStrengths.setText(getString(R.string.console_comparison_strengths_placeholder));
            return;
        }

        txtComparisonSubtitle.setText(getString(R.string.console_comparison_result_subtitle));
        txtPriceComparison.setText(formatearPrecio(selectedOne, selectedTwo));
        txtPowerComparison.setText(formatearPotencia(selectedOne, selectedTwo));
        txtExclusiveComparison.setText(formatearExclusivos(selectedOne, selectedTwo));
        txtServiceComparison.setText(formatearServicios(selectedOne, selectedTwo));
        txtConsoleOneStrengths.setText(formatearFortalezas(selectedOne, selectedTwo));
        txtConsoleTwoStrengths.setText(formatearFortalezas(selectedTwo, selectedOne));
    }

    private String formatearPrecio(ConsoleInfo primera, ConsoleInfo segunda) {
        double diferencia = primera.priceUsd - segunda.priceUsd;
        if (Math.abs(diferencia) < 0.01) {
            return String.format(Locale.getDefault(), "Precio: empate en $%.0f USD.", primera.priceUsd);
        }
        ConsoleInfo barata = diferencia < 0 ? primera : segunda;
        ConsoleInfo cara = diferencia < 0 ? segunda : primera;
        return String.format(Locale.getDefault(), "Precio: %s es $%.0f USD más barata que %s.",
                barata.name, Math.abs(diferencia), cara.name);
    }

    private String formatearPotencia(ConsoleInfo primera, ConsoleInfo segunda) {
        double diferencia = primera.powerTflops - segunda.powerTflops;
        if (Math.abs(diferencia) < 0.01) {
            return String.format(Locale.getDefault(), "Potencia: empate en %.1f TFLOPS.", primera.powerTflops);
        }
        ConsoleInfo mejor = diferencia > 0 ? primera : segunda;
        ConsoleInfo menor = diferencia > 0 ? segunda : primera;
        return String.format(Locale.getDefault(), "Potencia: %s supera a %s por %.1f TFLOPS.",
                mejor.name, menor.name, Math.abs(diferencia));
    }

    private String formatearExclusivos(ConsoleInfo primera, ConsoleInfo segunda) {
        int diferencia = primera.exclusivesScore - segunda.exclusivesScore;
        if (diferencia == 0) {
            return "Exclusivos: empate en catálogo.";
        }
        ConsoleInfo mejor = diferencia > 0 ? primera : segunda;
        ConsoleInfo menor = diferencia > 0 ? segunda : primera;
        return String.format(Locale.getDefault(), "Exclusivos: %s tiene %d puntos más que %s.",
                mejor.name, Math.abs(diferencia), menor.name);
    }

    private String formatearServicios(ConsoleInfo primera, ConsoleInfo segunda) {
        int diferencia = primera.servicesScore - segunda.servicesScore;
        if (diferencia == 0) {
            return "Servicios: empate en propuestas online.";
        }
        ConsoleInfo mejor = diferencia > 0 ? primera : segunda;
        ConsoleInfo menor = diferencia > 0 ? segunda : primera;
        return String.format(Locale.getDefault(), "Servicios: %s ofrece mejor servicio (+%d).",
                mejor.name, Math.abs(diferencia));
    }

    private String formatearFortalezas(ConsoleInfo principal, ConsoleInfo otra) {
        List<String> fortalezas = new ArrayList<>();
        if (principal.priceUsd < otra.priceUsd) {
            fortalezas.add("precio");
        }
        if (principal.powerTflops > otra.powerTflops) {
            fortalezas.add("potencia");
        }
        if (principal.exclusivesScore > otra.exclusivesScore) {
            fortalezas.add("exclusivos");
        }
        if (principal.servicesScore > otra.servicesScore) {
            fortalezas.add("servicios");
        }
        String texto = fortalezas.isEmpty() ? "sin ventaja clara" : String.join(", ", fortalezas);
        return String.format(Locale.getDefault(), "%s destaca en: %s.", principal.name, texto);
    }

    private List<ConsoleInfo> obtenerConsolas() {
        return Arrays.asList(
                new ConsoleInfo("PlayStation 5", 499, 10.3, 9, 8),
                new ConsoleInfo("Xbox Series X", 499, 12.0, 7, 9),
                new ConsoleInfo("Nintendo Switch OLED", 349, 4.0, 10, 6),
                new ConsoleInfo("PC Gamer", 899, 14.0, 8, 7)
        );
    }
}
