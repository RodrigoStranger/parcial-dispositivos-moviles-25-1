# Programación Para Dispositivos Móviles - Examen Parcial

📅 **Ciclo Académico**: 2025 - I  
🎓 **Semestre**: VI  
👨‍🏫 **Profesor**: Josue Miguel Flores Parra  
✍ **Autor**: Rodrigo Emerson Infanzón Acosta  

---

## 📌 Descripción del Proyecto

El juego consta del reconocimiento de colores donde el usuario debe presionar el botón que coincida con el color mostrado en pantalla.

---

## 🔗 Documentación (DeepWiki)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/RodrigoStranger/parcial-dispositivos-moviles-25-1)

---

### 🎯 Objetivo
El objetivo es lograr la mayor cantidad de aciertos posibles en 30 segundos.

---

### 🧩 ¿Qué debe tener la aplicación?

1. **Fragmento de bienvenida (WelcomeFragment)**:
   
   🎮 Título del juego  
   📖 Breve explicación del juego  
   ▶️ Botón “Iniciar juego” para comenzar  
   🖼️ Interfaz libre, pueden incluir imágenes

2. **Fragmento del juego (GameFragment):**
   
   🟦 Cuadro que muestra un color (rojo, verde, azul, amarillo, etc)  
   🟢🔵🔴🟡 Botones de colores para responder  
   ✅ Cada acierto suma un punto y cambia el color  
   ⏰ Temporizador de 30 segundos  
   🏆 Marcador que muestra los puntos obtenidos

3. **Fragmento de resultados (ResultFragment):**
   
   🏁 Muestra el puntaje final  
   💬 Muestra un mensaje según el puntaje obtenido  
   🔄 Botón para volver a jugar

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
- Usa recursos de colores en [`colors.xml`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/values/colors.xml).
- Puedes usar una función para generar el color aleatorio.
- Para el temporizador, revisa la clase `CountDownTimer`.

---

## 🛠️ Explicación Técnica del Proyecto

### ✅ Cumplimiento de Requisitos Técnicos
- 🔊 **Uso de sonidos:** Se implementaron efectos de sonido para aciertos, errores, advertencia, puntuación y música de fondo, usando archivos en [`res/raw/`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/tree/main/app/src/main/res/raw) y la clase `MediaPlayer`.
- 🖼️ **Imágenes y animaciones:** Se utilizaron imágenes y animaciones en la interfaz, como animaciones de rebote en los botones ([`res/anim/boton_bounce.xml`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/anim/boton_bounce.xml)) y recursos visuales en los layouts.
- ⏰ **Temporizador:** Se implementó el temporizador usando la clase `CountDownTimer` (`import android.os.CountDownTimer`), cumpliendo el requisito de limitar el tiempo de juego y dar feedback visual/sonoro.

### 1. 🗂️ Estructura y Navegación
- 🏠 [**MainActivity.kt:**](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/MainActivity.kt) Centraliza la gestión de la música de fondo (líneas 27-70), inicializa la app y configura la pantalla (línea 72 en adelante).
- 👋 [**WelcomeFragment.kt:**](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/WelcomeFragment.kt) 
Muestra la pantalla de bienvenida, controla la música y la orientación (líneas 14-66). El botón de inicio anima y navega a GameFragment.
- 🎮 [**GameFragment.kt:**](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/GameFragment.kt) Núcleo del juego (líneas 18-381). Gestiona la lógica de colores, botones, puntaje, temporizador y sonidos.
- 🏁 [**ResultFragment.kt:**](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/ResultFragment.kt) Muestra el resultado final, reproduce sonido y permite volver al menú o reiniciar (líneas 10-112). Bloquea el botón físico atrás.

### 2. 🎵 ¿Por qué GameFragment tiene su propio MediaPlayer?
- **GameFragment** utiliza instancias locales de MediaPlayer para efectos cortos e inmediatos (acierto, error, advertencia, fin de ronda) sin interferir con la música de fondo global.
- Esto permite reproducir varios sonidos simultáneos y liberar recursos rápidamente (líneas 19-25, 41-75 y 330-381 en GameFragment.kt).
- La música de fondo, en cambio, es global y persistente, por eso se gestiona en MainActivity.

### 3. 🗃️ Recursos Utilizados
- 🔊 **Sonidos**: Archivos en [`res/raw/`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/tree/main/app/src/main/res/raw) como [`sonido_correcto`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/sonido_correcto.mp3), [`sonido_error`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/sonido_error.mp3), [`sonido_advertencia`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/sonido_advertencia.mp3), [`sonido_puntuacion`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/sonido_puntuacion.mp3), y música de fondo ([`musica_de_fondo_inicio`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/musica_de_fondo_inicio.mp3), etc.). Usados con MediaPlayer en los fragments y la actividad principal.
- 🖼️ **Imágenes**: Se usaron imágenes en [`res/drawable/`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/tree/main/app/src/main/res/drawable) como recursos visuales para feedback y UI.
- 🎞️ **Animaciones**: Archivos en [`res/anim/`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/tree/main/app/src/main/res/anim) como [`boton_bounce`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/anim/boton_bounce.xml), aplicados con `AnimationUtils` en botones de WelcomeFragment, ResultFragment y GameFragment.
- 🎨 **Colores**: Definidos en [`res/values/colors.xml`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/values/colors.xml) (ejemplo: rojo, azul, verde, amarillo, etc.), asignados dinámicamente a los botones en GameFragment.
- 📝 **Strings y layouts**: Uso de [`strings.xml`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/values/strings.xml) y layouts XML para mantener el texto y la interfaz desacoplados del código.
- ⚙️ **Constantes y utilidades**: Uso de funciones utilitarias y constantes para evitar duplicidad y facilitar el mantenimiento.

### 4. 🧑‍💻 Consideraciones de UX y Ciclo de Vida
- ⛔ **Bloqueo del botón atrás**: En GameFragment y ResultFragment se bloquea el botón físico "atrás" para evitar que el usuario salga accidentalmente durante una partida o al ver resultados.
- 📲 **Manejo en segundo plano**: El temporizador del juego y los recursos de audio se pausan, reanudan o liberan correctamente cuando la app va a segundo plano o el usuario navega fuera del fragmento, evitando errores y fugas de memoria.
- 🔄 **Ciclo de vida robusto**: Toda la gestión de recursos y estados considera los eventos de pausa, reanudación y destrucción de vistas, para asegurar una experiencia fluida y sin problemas.

### 5. 🔎 Lógica del Juego (GameFragment y ResultFragment)
- 🎨 De los 10 colores definidos en [`colors.xml`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/values/colors.xml), en cada ronda se seleccionan aleatoriamente 4 colores diferentes para los botones.
- 🎯 De esos 4 colores, se elige uno al azar como "color objetivo" y se muestra su nombre en el campo correspondiente.
- 👆 El usuario debe presionar el botón cuyo color coincida con el nombre mostrado.
- ✅ Si la selección es correcta:
  - ➕ Se suma 1 punto al marcador.
  - 🟢 Se muestra la imagen [`bien.png`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/drawable/bien.png) en el `ImageView` y se reproduce [`sonido_correcto.mp3`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/sonido_correcto.mp3).
  - 🕒 La imagen desaparece automáticamente al finalizar el sonido.
- ❌ Si la selección es incorrecta:
  - 🔴 Se muestra la imagen [`error.png`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/drawable/error.png) y se reproduce [`sonido_error.mp3`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/res/raw/sonido_error.mp3).
  - 🕒 La imagen desaparece al finalizar el sonido.
- 🔄 Tras cada intento (acierto o error), se inicia una nueva ronda con 4 colores aleatorios.
- ⏰ Este ciclo se repite hasta que el temporizador (`CountDownTimer`) llega a cero.

**¿Cómo se valida la respuesta?**
- 🏷️ Cada botón tiene asignado un "tag" con el nombre del color que representa.
- 👆 Cuando el usuario presiona un botón, se compara el nombre del color objetivo (por ejemplo, "Azul") con el tag del botón presionado.
- ✅ Si ambos nombres coinciden, se considera un acierto.
- ❌ Si no coinciden, se considera un error.

**¿Cómo se pasa y recibe el puntaje final?**
- 🏁 Al terminar el juego, en [`GameFragment`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/GameFragment.kt) (líneas 357-381), se crea un `Bundle` y se agrega el puntaje con la clave "score":
  ```kotlin
  val bundle = Bundle()
  bundle.putInt("score", score)
  findNavController().navigate(R.id.action_gameFragment_to_resultFragment, bundle)
  ```
- 📥 En [`ResultFragment`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/ResultFragment.kt) (línea 23), se recupera el puntaje recibido:
  ```kotlin
  val score = arguments?.getInt("score") ?: 0
  ```
- 🖥️ Luego se muestra el puntaje en pantalla usando un `TextView` (líneas 26-29), y se reproduce el sonido de puntuación.
---

### 🧩 Lógica de los Botones en ResultFragment
En [`ResultFragment`](https://github.com/RodrigoStranger/parcial-dispositivos-moviles-25-1/blob/main/app/src/main/java/com/example/juegodecolores/ResultFragment.kt) (líneas 61-70) se define la lógica de los dos botones principales:
- 🔄 Uno para reiniciar el juego.
   ```kotlin
   override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                       // Detengo la música y regreso al fragmento anterior para reiniciar el juego.
                       (activity as? MainActivity)?.detenerMusicaFondo()
                       findNavController().popBackStack()
                   }
   ```
- 🏠 Otro para volver al menú principal.
  ```kotlin
   override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    // Detengo y libero el sonido de puntuación para evitar que siga sonando.
                    puntuacionPlayer?.stop()
                    puntuacionPlayer?.release()
                    puntuacionPlayer = null

                    // Cambio la música de fondo a la del menú principal.
                    (activity as? MainActivity)?.detenerMusicaFondo()
                    (activity as? MainActivity)?.iniciarMusicaFondo(R.raw.musica_de_fondo_inicio)

                    // Navego hacia el fragmento de bienvenida.
                    findNavController().navigate(R.id.action_resultFragment_to_welcomeFragment)
                }

   ```
## 📊 Análisis de Resultados
- 🏆 **Puntaje Final:** El puntaje final se muestra en el fragmento de resultados, y se reproduce el sonido de puntuación.
- 💬 **Mensajes de Resultado:** Dependiendo del puntaje obtenido, se muestra un mensaje de resultado.
- 🔁 **Botones de Acción:** En el fragmento de resultados, se muestra un botón para volver a jugar y otro para volver al menú principal.
