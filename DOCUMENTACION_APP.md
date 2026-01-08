# Documentación de persistencia y flujo

## Almacenamiento local

- **SQLite (pelis_app.db)**: gestionado por `datos/db/AyudanteBaseDatosSQLite.java`.
  - `watchlist`: guarda las películas marcadas como "Ver después" (`pelicula_id` único, metadatos básicos, géneros y plataformas serializados en JSON, `fecha_guardado` como timestamp).
  - `descartadas`: guarda los identificadores de películas marcadas como "No me interesa" con un `fecha_descartado`.
- **Repositorios**:
  - `datos/db/RepositorioWatchlistSQLite.java`: CRUD de la tabla `watchlist` (guardar, eliminar, comprobar existencia, listar).
  - `datos/db/RepositorioDescartadasSQLite.java`: inserta, consulta y obtiene el conjunto de IDs descartadas.
- **Preferencias de usuario**: se mantienen en `SharedPreferences` con Gson (`datos/preferencias/GestorPreferenciasUsuario.java` y `PreferenciasUsuario`). Guarda onboarding completado y las selecciones de géneros/plataformas/evitar.

## Motor de recomendaciones

- `datos/MotorRecomendaciones.java` lee `assets/peliculas.json` (mínimo 20 películas) y genera una lista ordenada (top 10) calculando un puntaje por coincidencia de mood, géneros, plataformas y duración.
- Antes de puntuar, filtra cualquier película cuyo `id` esté en `descartadas` o que coincida con criterios de "evitar" (violencia/gore, duraciones largas si se marcó "películas lentas", sinopsis con palabras asociadas a finales tristes).
- El resultado alimenta `RecomendacionesFragment`, que muestra las películas en un `RecyclerView`.

## Flujo de pantallas

1. **Onboarding** (`OnboardingFragment`): selecciona géneros, plataformas y elementos a evitar. Guarda preferencias y marca el flag de onboarding. El botón "Saltar" sólo marca el flag.
2. **Inicio** (`InicioFragment`): elige tiempo, mood y compañía. Al pulsar "Buscar película" abre **Recomendaciones**.
3. **Recomendaciones** (`RecomendacionesFragment`): muestra sugerencias (ya filtradas por descartadas). Al tocar un ítem abre **Detalle**.
4. **Detalle de película** (`DetallePeliculaFragment`):
   - Botón "Ver después": guarda en SQLite usando `RepositorioWatchlistSQLite`.
   - Botón "No me interesa": marca en SQLite con `RepositorioDescartadasSQLite`, lanza un `FragmentResult` para que la lista anterior la elimine y hace pop del back stack.
5. **Ver después** (`VerDespuesFragment`): lista la watchlist desde SQLite, permite eliminar con long-press y abrir el detalle con tap.
6. **Modo grupo** (`ModoGrupoFragment`): placeholder informativo; accesible desde Inicio.
7. **Toolbar**: acceso rápido a "Ver después" en cualquier pantalla.

Todas las operaciones de SQLite se ejecutan en segundo plano con `ExecutorService` y devuelven resultados a la UI mediante `runOnUiThread` para evitar bloqueos.
