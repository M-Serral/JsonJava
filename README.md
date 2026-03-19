# JsonJava

Un proyecto Java Maven diseñado para Software Development Engineer in Test (SDET) que extrae datos de clientes desde una
base de datos MySQL y los convierte en archivos JSON para la generación de datos de prueba, especialmente útil en
escenarios de automatización móvil con Appium.

## Descripción

Este proyecto facilita la transición de datos relacionales (MySQL) a formatos estructurados (JSON) para pruebas
automatizadas.

El flujo actual implementado es:

Base de Datos MySQL → Objetos Java (POJOs) → JSON estructurado → Deserialización selectiva a objetos Java

Los datos se agrupan en un único JSON bajo una clave `data`, permitiendo trabajar con colecciones completas de registros
para escenarios data-driven.

Es ideal para entornos de testing donde necesitas datos dinámicos, mantenibles y desacoplados del código de pruebas.

## Modificaciones Recientes

- **jsonToJava.java**: Actualizado para conectar a la base de datos con credenciales `dev`/`dev`, ejecutar consulta
  `SELECT * FROM CustomerInfo WHERE Location = 'Asia'`, mapear resultados a objetos `CustomerDetails`, almacenarlos en
  un `ArrayList`, y serializar a JSON con estructura `{"data": [lista]}` en `customerData.json`.

- **extractJson.java**: Actualizado para leer `customerData.json` como `Map<String, Object>`, extraer la lista bajo la
  clave `"data"`, obtener el primer elemento, convertirlo dinámicamente a `CustomerDetailsAppium` usando
  `ObjectMapper.convertValue()`, e imprimir el `courseName`.

## Características

- Extracción de Datos: Conexión a MySQL y ejecución de queries filtradas (por Location = 'Asia').
- Serialización estructurada a JSON:
    - Generación de un único archivo `customerData.json`.
    - Uso de estructura tipo colección: `"data": [ ... ]`.
- Deserialización flexible:
    - Lectura de JSON como `Map<String, Object>`.
    - Conversión dinámica a POJOs con Jackson (`convertValue`).
- POJOs especializados:
    - `CustomerDetails`: Campos básicos (courseName, purchasedDate, amount, location).
    - `CustomerDetailsAppium`: Extiende `CustomerDetails` con `studentName`.
- Preparado para Data-Driven Testing.
- Integración con Appium.
- Desacoplamiento de datos.

## Arquitectura

### Flujo de Datos

1. Conexión a MySQL (`127.0.0.1:3306/Business`, usuario: `dev`, password: `dev`).
2. Ejecución de consulta SQL: `SELECT * FROM CustomerInfo WHERE Location = 'Asia';`.
3. Mapeo a `CustomerDetails`.
4. Almacenamiento en `ArrayList<CustomerDetails>`.
5. Creación de `Map<String, Object>` con clave `"data"` conteniendo la lista.
6. Serialización a JSON y escritura en `customerData.json`.
7. Lectura del JSON como `Map`.
8. Extracción de la lista `"data"`.
9. Obtención del primer elemento (`data.getFirst()`).
10. Conversión a `CustomerDetailsAppium` usando `convertValue`.
11. Uso del objeto (ej. imprimir `courseName`).

### Componentes Clave

- `jsonToJava.java`: Maneja la extracción de DB y serialización a JSON.
- `extractJson.java`: Maneja la deserialización de JSON y conversión a POJO.
- `CustomerDetails.java`: POJO básico con getters/setters.
- `CustomerDetailsAppium.java`: POJO extendido para Appium.

### Ejemplo de JSON Generado

```json
{
  "data": [
    {
      "courseName": "Appium",
      "purchasedDate": "2026-03-13",
      "amount": 99,
      "location": "Asia"
    },
    {
      "courseName": "WebServices",
      "purchasedDate": "2026-03-13",
      "amount": 21,
      "location": "Asia"
    },
    {
      "courseName": "Jmeter",
      "purchasedDate": "2026-03-13",
      "location": "Asia",
      "amount": 76
    }
  ]
}
```

## Dependencias

- **Jackson Core, Databind, Annotations 2.15.2**: Para procesamiento JSON.
- **MySQL Connector 8.0.19**: Para conectividad a DB (scope runtime).

## Requisitos

- Java 11+
- Maven 3.x
- MySQL corriendo en `localhost:3306`
- Base de datos: `Business`
- Tabla: `CustomerInfo` con columnas: courseName, purchasedDate, amount, location
- Usuario DB: `dev`
- Password DB: `dev`

## Instalación

```bash
git clone <url-del-repositorio>
cd JsonJava
```

## Uso

### Compilar el Proyecto

```bash
mvn clean compile
```

### Ejecutar Extracción y Generación de JSON

```bash
mvn exec:java -Dexec.mainClass="jsonToJava"
```

Esto generará `customerData.json` en la raíz del proyecto.

### Ejecutar Deserialización y Uso de Datos

```bash
mvn exec:java -Dexec.mainClass="extractJson"
```

Esto leerá `customerData.json`, extraerá el primer registro, lo convertirá a `CustomerDetailsAppium` e imprimirá el
`courseName`.

## Testing

En un proyecto de Appium, puedes usar los objetos deserializados para alimentar datos en pruebas móviles, por ejemplo,
creando usuarios dinámicos basados en los datos de `CustomerDetailsAppium`.

## Troubleshooting

- **Conexión MySQL**: Asegúrate de que MySQL esté corriendo y la DB `Business` exista con datos en `CustomerInfo`.
- **Java Version**: El proyecto requiere Java 11+. Verifica con `java -version`.
- **Archivos JSON**: Si no se generan, revisa logs de ejecución y permisos de escritura.

## Licencia

Open Source
