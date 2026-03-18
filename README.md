# JsonJava

Un proyecto Java Maven diseñado para Software Development Engineer in Test (SDET) que extrae datos de clientes desde una base de datos MySQL y los convierte en archivos JSON para la generación de datos de prueba, especialmente útil en escenarios de automatización móvil con Appium.

## Descripción

Este proyecto facilita la transición de datos relacionales (MySQL) a formatos estructurados (JSON) para pruebas automatizadas. El flujo principal es: Base de Datos MySQL → Objetos Java (POJOs) → Archivos JSON → Deserialización de vuelta a objetos Java para uso en pruebas.

Es ideal para entornos de testing donde necesitas datos realistas y actualizables sin hardcodear valores en el código de pruebas.

## Características

- **Extracción de Datos**: Conecta a MySQL y ejecuta consultas para filtrar datos (ej. por ubicación o fecha).
- **Serialización a JSON**: Convierte objetos Java a archivos JSON usando Jackson, con mapeo automático de campos (PascalCase en Java a camelCase en JSON).
- **Deserialización**: Ejemplos para leer JSON de vuelta a objetos Java.
- **POJOs Especializados**: Incluye clases como `CustomerDetails` (básica) y `CustomerDetailsAppium` (extendida para pruebas móviles).
- **Integración con Appium**: Los JSON sirven como fuente de datos para automatización de pruebas móviles, permitiendo escenarios data-driven.
- **Flexibilidad**: Fácil de extender para nuevos campos o consultas.

## Arquitectura

- **Flujo de Datos**:
  1. Conexión a MySQL (`Business` schema, tabla `CustomerInfo`).
  2. Ejecución de consulta SQL (ej. `SELECT * FROM CustomerInfo WHERE Location = 'Asia'`).
  3. Mapeo de resultados a POJOs (`CustomerDetails`).
  4. Serialización a JSON con ObjectMapper.
  5. Almacenamiento en archivos (ej. `CUSTOMERINFO0.json`).
  6. Deserialización opcional para uso en pruebas.

- **Componentes Clave**:
  - `jsonToJava.java`: Clase principal para extracción y serialización.
  - `extractJson.java`: Ejemplo de deserialización.
  - `CustomerDetails.java`: POJO básico (courseName, purchasedDate, Amount, Location).
  - `CustomerDetailsAppium.java`: POJO extendido con `studentName` para Appium.

- **Dependencias**:
  - Jackson (2.15.2): Para procesamiento JSON.
  - MySQL Connector (8.0.19): Para conectividad DB.

## Requisitos Previos

- **Java**: JDK 11 o superior (recomendado JDK 17 para compatibilidad).
- **Maven**: Para gestión de dependencias y builds.
- **MySQL**: Instancia corriendo en `localhost:3306` con:
  - Base de datos: `Business`.
  - Tabla: `CustomerInfo` (columnas: courseName, purchasedDate, Amount, Location).
  - Credenciales: Usuario `dev`, contraseña `dev` (o ajusta en el código).
- **IDE**: IntelliJ IDEA recomendado para debugging y ejecución.

## Instalación y Configuración

1. **Clona el Repositorio**:
   ```bash
   git clone <url-del-repositorio>
   cd JsonJava
   ```

2. **Configura MySQL**:
   - Asegúrate de que MySQL esté corriendo.
   - Crea la DB y tabla si no existen:
     ```sql
     CREATE DATABASE Business;
     USE Business;
     CREATE TABLE CustomerInfo (
         courseName VARCHAR(255),
         purchasedDate DATE,
         Amount INT,
         Location VARCHAR(255)
     );
     -- Inserta datos de ejemplo
     INSERT INTO CustomerInfo VALUES ('Appium', '2026-03-13', 99, 'Asia');
     ```

3. **Compila el Proyecto**:
   - En IntelliJ: Abre el proyecto y deja que Maven resuelva dependencias.
   - O manualmente: `mvn clean compile` (nota: puede haber issues con repositorios Maven; ajusta `settings.xml` si es necesario).

4. **Ejecuta**:
   - Genera JSON: `mvn exec:java -Dexec.mainClass="jsonToJava"`
   - Deserializa: `mvn exec:java -Dexec.mainClass="extractJson"`

## Uso

### Generar Datos de Prueba
Ejecuta `jsonToJava` para extraer de DB y crear JSON:
- Archivos generados: `CUSTOMERINFO0.json`, `CUSTOMERINFO1.json`, etc.
- Salida en consola: Número de registros y confirmación de escritura.

### Usar en Pruebas Appium
1. Deserializa un JSON a objeto:
   ```java
   ObjectMapper mapper = new ObjectMapper();
   CustomerDetailsAppium user = mapper.readValue(new File("CUSTOMERINFO0.json"), CustomerDetailsAppium.class);
   System.out.println(user.getStudentName());  // Ejemplo de uso
   ```

2. Integra en tests móviles:
   - Usa `user.getAmount()` para validar precios en la app.
   - Parametriza tests con datos de JSON para escenarios múltiples.

### Ejemplos de Archivos JSON
- `CUSTOMERINFO0.json`:
  ```json
  {
    "courseName": "Appium",
    "purchasedDate": "2026-03-13",
    "location": "Asia",
    "amount": 99,
    "studentName": "John Doe"  // Campo extra en CustomerDetailsAppium
  }
  ```

## Utilidades

### A Nivel de Desarrollo
- **POJOs Reutilizables**: Las clases JavaBean siguen patrones estándar, facilitando extensiones (ej. agregar validaciones con Bean Validation).
- **Mapeo Automático**: Jackson maneja conversiones sin código extra, ideal para APIs REST o serialización.
- **Debugging**: Breakpoints en `jsonToJava` permiten inspeccionar datos de DB.
- **Escalabilidad**: Fácil agregar nuevas consultas o campos sin romper el flujo.

### A Nivel de Testing
- **Data-Driven Testing**: JSON como fuente externa para tests parametrizados, evitando dependencias de DB en ejecución.
- **Automatización Móvil**: En Appium, alimenta datos a formularios, logins o validaciones (ej. comparar `courseName` con UI).
- **Entornos Aislados**: Tests corren con JSON estáticos, ideales para CI/CD sin DB.
- **Mantenimiento**: Actualiza datos en DB y regenera JSON; tests se adaptan automáticamente.
- **Cobertura**: Soporta edge cases (ej. datos nulos, fechas pasadas) editando JSON manualmente.

## Contribución

1. Forkea el repo.
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`).
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`).
4. Push y crea un Pull Request.

Asegúrate de que los tests pasen y el código siga convenciones Java (ej. getters/setters).

## Troubleshooting

- **Errores de Maven**: Si falla la descarga de plugins, edita `~/.m2/settings.xml` y comenta secciones `<mirrors>` y `<activeProfiles>` para usar repos públicos.
- **Conexión DB**: Verifica que MySQL esté corriendo y las credenciales sean correctas.
- **Archivos No Generados**: Revisa la consola por excepciones; asegura que la consulta devuelva resultados.
- **Versiones Java**: Usa JDK 17 para evitar incompatibilidades.

## Licencia

Este proyecto es de código abierto. Consulta el archivo LICENSE para detalles.

---

¡Si tienes preguntas o necesitas ayuda para integrar con Appium, abre un issue en el repo!</content>
<parameter name="filePath">C:\WorkspacesMine\SDET_TestArchitech\JsonJava\README.md
