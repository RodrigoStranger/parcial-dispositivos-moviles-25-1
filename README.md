# Programación Para Dispositivos Móviles - Examen Parcial

📅 **Ciclo Académico**: 2025 - I  
🎓 **Semestre**: VI  
👨‍🏫 **Profesor**: Josue Miguel Flores Parra  
✍ **Autor**: Rodrigo Emerson Infanzón Acosta  

---

## 📌 Descripción del Proyecto

El juego consta del reconocimiento de colores donde el usuario debe presionar el botón que coincida con el color mostrado en pantalla.

---

### 🎯 Objetivo
El objetivo es lograr la mayor cantidad de aciertos posibles en 30 segundos.

---

### 🧩 ¿Qué debe tener la aplicación?

1. **Fragmento de bienvenida (WelcomeFragment):**
   - Título del juego.
   - Breve explicación del juego.
   - Botón “Iniciar juego” para comenzar.
   - Interfaz libre, pueden incluir imágenes.

2. **Fragmento del juego (GameFragment):**
   - Cuadro que muestra un color (rojo, verde, azul, amarillo, etc).
   - Botones de colores para responder.
   - Cada acierto suma un punto y cambia el color.
   - Temporizador de 30 segundos.
   - Marcador que muestra los puntos obtenidos.

3. **Fragmento de resultados (ResultFragment):**
   - Muestra el puntaje final.
   - Muestra un mensaje según el puntaje obtenido.
   - Botón para volver a jugar.

---

### 🧩 Funcionalidades Adicionales (elige al menos una, es obligatoria):
- 🔊 Agrega sonidos al juego (por ejemplo, cuando acierta, se equivoca o música de fondo).
- 💾 Guarda el puntaje más alto usando SharedPreferences.
- 🎨 Aplica animaciones a los botones o colores que aparecen.
- 🧠 Implementa niveles de dificultad (Ejemplo: Efecto Stroop, más colores conforme avanzas, etc).

---

### 🧪 ¿Qué se debe practicar?
- Navegación entre Fragments y paso de datos.
- Uso de temporizador (`CountDownTimer`).
- Interacción con el usuario a través de botones y feedback.
- Manejo de estados y lógica condicional básica.
- Diseño de UI con ConstraintLayout.
- Control de errores y validación del input del usuario.
- Organización del código y buenas prácticas de programación en Kotlin.
- Uso adecuado de recursos visuales (colores, imágenes, etc).
- Almacenamiento básico de datos usando SharedPreferences (si eliges esa funcionalidad adicional).
- Incorporación de medios como sonidos o animaciones opcionales (si eliges esa funcionalidad adicional).

---

### 🧠 Tips útiles
- Usa recursos de colores en `colors.xml`.
- Puedes usar una función para generar el color aleatorio.
- Para el temporizador, revisa la clase `CountDownTimer`.

