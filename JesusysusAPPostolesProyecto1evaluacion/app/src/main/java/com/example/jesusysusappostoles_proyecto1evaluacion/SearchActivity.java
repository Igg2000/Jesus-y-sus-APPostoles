package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {  // Extiende AppCompatActivity

    private EditText editTextCity;
    private Button buttonSearch;
    private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity); // Asegúrate de que este archivo XML exista

        editTextCity= findViewById(R.id.cityEditText);
        buttonSearch= findViewById(R.id.searchButton);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName= editTextCity.getText().toString().trim();
                if (!cityName.isEmpty()){
                    try {
                        performSPARQLQuery(cityName);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Por favor, introduce un nombre de ciudad.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void performSPARQLQuery(String cityName) throws UnsupportedEncodingException {
        String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
        String query = String.format(
                "SELECT ?lugar ?lugarLabel ?population ?latitude ?longitude WHERE { " +
                        "?lugar rdfs:label \"%s\"@es; " +  // Usar el nombre exacto en español
                        "wdt:P1082 ?population; " +       // Población
                        "wdt:P625 ?location. " +          // Coordenadas (ubicación)
                        "BIND(geof:latitude(?location) AS ?latitude). " +
                        "BIND(geof:longitude(?location) AS ?longitude). " +
                        "SERVICE wikibase:label { bd:serviceParam wikibase:language \"es\". } " + // Obtener label en español
                        "} " +
                        "LIMIT 1",
                encodedCityName
        );

        String url = "https://query.wikidata.org/sparql?query=" + URLEncoder.encode(query, "UTF-8") + "&format=json";

        Log.d("SearchActivity", "URL de la consulta: " + url);
        Log.d("SearchActivity", "Consulta SPARQL: " + query);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SearchActivity", "Error en la solicitud: " + e, e);
                runOnUiThread(() -> Toast.makeText(SearchActivity.this, "Error en la conexión.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error en la respuesta: " + response);
                }

                String responseData = response.body().string();
                Log.d("SearchActivity", "Respuesta del servidor: " + responseData);

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray results = jsonObject.getJSONObject("results").getJSONArray("bindings");

                    if (results.length() > 0) {
                        JSONObject firstResult = results.getJSONObject(0);

                        String cityName = firstResult.getJSONObject("lugarLabel").getString("value");  // Nombre de la ciudad
                        String population = firstResult.has("population") ? firstResult.getJSONObject("population").getString("value") : "No disponible";
                        String latitude = firstResult.has("latitude") ? firstResult.getJSONObject("latitude").getString("value") : "No disponible";
                        String longitude = firstResult.has("longitude") ? firstResult.getJSONObject("longitude").getString("value") : "No disponible";

                        runOnUiThread(() -> {
                            // Mostrar la información al usuario
                            showResult(cityName, population, latitude, longitude);
                        });
                    } else {
                        runOnUiThread(() -> showErrorDialog("Ciudad no encontrada."));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> showErrorDialog("Error al procesar los datos."));
                }
            }
        });
    }

    private void showResult(String encodedCityName, String population, String latitude, String longitude) {
        String resultMessage = "Ciudad: " + encodedCityName + "\n" +
                "Población: " + population + "\n" +
                "Latitud: " + latitude + "\n" +
                "Longitud: " + longitude;
        // Muestra el mensaje en el TextView
        new AlertDialog.Builder(this)
                .setTitle("Información de la Ciudad")
                .setMessage(resultMessage)
                .setPositiveButton("Aceptar", null) // Botón para cerrar el diálogo
                .show();
    }
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
