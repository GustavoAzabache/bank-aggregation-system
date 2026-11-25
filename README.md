# 🏦 BankSync AI — Agregador Financiero Reactivo con Tool Calling

## 🎯 ¿De qué trata el proyecto?
**BankSync AI** es una aplicación web full-stack diseñada para centralizar, normalizar y consultar historiales de movimientos bancarios provenientes de múltiples entidades financieras que exportan sus datos en formatos heterogéneos (JSON, XML, HTML, CSV). 

El sistema ingesta grandes volúmenes de archivos de forma asíncrona y no bloqueante, los estandariza en una base de datos relacional reactiva y expone un asistente conversacional impulsado por Inteligencia Artificial (*Tool Calling*) capaz de consultar los registros y exportarlos en el formato que el usuario solicite.

---

## 💡 Problema que soluciona y Utilidad
Las personas con cuentas en múltiples bancos (ej. BCP, Interbank, BBVA) suelen descargar trimestralmente sus estados de cuenta para llevar un control financiero. Sin embargo:
1. **Fragmentación de formatos:** Cada banco entrega los datos con estructuras distintas (tablas HTML, archivos XML, JSON o CSV).
2. **Análisis manual tedioso:** Consolidar miles de registros manualmente en hojas de cálculo consume tiempo y es propenso a errores.
3. **Consultas rígidas:** Filtrar movimientos específicos y volver a exportarlos en otro formato suele requerir conocimientos técnicos o herramientas complejas.

**Utilidad:** Automatiza la unificación de extractos bancarios en segundos y permite consultar finanzas personales usando lenguaje natural (ej. *"Dame mis gastos mayores a 3000 soles del último trimestre en formato XML"*).

---

## ✅ Alcance del Proyecto (Lo que SÍ abarcará)
* **Ingesta Reactiva y Asíncrona:** Endpoint que recibe múltiples archivos simultáneamente (`Flux<FilePart>`), responde inmediatamente con `HTTP 202 Accepted` y procesa los documentos en segundo plano sin bloquear hilos.
* **Motor de Parseo Extensible (Patrón Strategy):** Detección automática del tipo de archivo y extracción de datos para formatos **JSON, XML, HTML y CSV** hacia un modelo unificado.
* **Persistencia Reactiva por Lotes:** Almacenamiento eficiente de miles de registros en PostgreSQL utilizando **Spring Data R2DBC** y procesamiento en lotes (`buffer`).
* **Consultas Inteligentes (Tool Calling):** Integración con **Spring AI** (vía Gemini API) para interpretar peticiones en lenguaje natural, ejecutar consultas filtradas en la base de datos y formatear la salida (JSON, XML, CSV).
* **Interfaz Web Funcional:** Frontend en **React + TypeScript** con carga múltiple de archivos (Drag & Drop), vista básica de estado y un chat interactivo para consultar al asistente.

---

## ⛔ Fuera de Alcance (Lo que NO abarcará)
* **Conexión directa a APIs bancarias (Open Banking):** Los archivos se subirán de forma manual por el usuario; no habrá integración directa ni *web scraping* contra portales bancarios reales.
* **Reconciliación bancaria contable:** No se cruzarán los extractos contra facturas internas, boletas ni sistemas ERP.
* **Seguridad empresarial avanzada:** No se implementará autenticación OAuth2/JWT multi-tenant ni cifrado bancario de grado productivo (se trabajará en entorno de un solo usuario para priorizar la arquitectura reactiva y de IA).
* **Dashboards financieros complejos:** El frontend se enfocará en la carga de archivos y la interacción con el chat de IA, evitando gráficos estadísticos avanzados.

---

## 🛠️ Stack Tecnológico
* **Backend:** Java 21, Spring Boot 3, Spring WebFlux, Spring Data R2DBC, Spring AI, Jackson XML, Jsoup, OpenCSV, Flyway.
* **Base de Datos:** PostgreSQL.
* **Frontend:** React, TypeScript, Vite, Tailwind CSS.
* **Infraestructura:** Docker & Docker Compose.
