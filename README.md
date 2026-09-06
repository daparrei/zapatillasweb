# Zapatillas Web API

## 📋 Descripción

API REST desarrollada con **Java y Spring Boot** para la gestión de una tienda de zapatillas.

El sistema permite administrar clientes, productos y stock por talle, así como gestionar comprobantes de compra con múltiples detalles.

La aplicación implementa reglas de negocio para el control automático de stock, cálculo de importes y totales, manejo de estados de comprobantes, consumo de servicios REST externos y logging mediante AOP.

Proyecto desarrollado como entrega final del curso de Java.

---

## 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Spring AOP
- REST API
- Git
- GitHub

---

# 🏗️ Arquitectura

El proyecto utiliza una arquitectura en capas para separar responsabilidades:

- **Controller:** Expone los endpoints REST y recibe las solicitudes HTTP.
- **Service:** Contiene la lógica y reglas de negocio.
- **Repository:** Gestiona el acceso y persistencia de datos.
- **Entity:** Representa las entidades del dominio.
- **API:** Contiene la integración con servicios REST externos.
- **Aspect:** Implementa funcionalidades transversales, como logging.

### Estructura del proyecto

```text
src/main/java/com/example/zapatillasweb
│
├── api
├── aspect
├── controller
├── entity
├── repository
├── service
│
└── ZapatillaswebApplication.java
```

---

# 👟 Funcionalidades principales

## 👤 Gestión de Clientes

El sistema permite:

- Crear clientes.
- Consultar todos los clientes.
- Consultar un cliente por ID.
- Actualizar clientes.
- Eliminar clientes.

---

## 👟 Gestión de Productos

Permite administrar los productos disponibles en la tienda:

- Crear productos.
- Consultar productos.
- Consultar productos por ID.
- Actualizar productos.
- Eliminar productos.

Cada producto posee información como:

- Nombre.
- Categoría.
- Precio.
- Descripción.
- Imagen.
- Stock disponible por talle.

---

## 📦 Gestión de Stock por Talle

Cada producto puede tener diferentes cantidades de stock según el talle.

Ejemplo:

| Producto | Talle | Stock |
|---|---:|---:|
| Enforcer | 40 | 10 |
| Enforcer | 41 | 5 |
| Enforcer | 42 | 8 |

El sistema administra automáticamente el stock cuando se crean o modifican detalles de comprobantes.

---

# 🧾 Gestión de Comprobantes

Un comprobante representa una compra realizada por un cliente.

Cada comprobante contiene:

- Cliente.
- Fecha de creación.
- Estado.
- Total.
- Lista de detalles.

Los estados posibles son:

- NUEVO
- PENDIENTE
- PAGADO
- ENTREGADO
- CANCELADO

---

## 📄 Gestión de Comprobante Detalle

Cada comprobante puede contener múltiples productos.

Un detalle contiene:

- Producto.
- Talle.
- Cantidad.
- Precio de compra.

### Validaciones implementadas

Al crear o actualizar un detalle, el sistema valida:

- Que el comprobante exista.
- Que el producto exista.
- Que el talle sea válido.
- Que la cantidad sea mayor a cero.
- Que exista stock suficiente.
- Que el comprobante permita modificaciones según su estado.
- Que el detalle pertenezca al comprobante correspondiente.

Durante la actualización de un detalle no se permite modificar:

- El producto.
- El talle.

Solo se permite actualizar la cantidad solicitada.

---

# 📦 Actualización automática de Stock

El stock se actualiza automáticamente según las operaciones realizadas sobre los detalles de un comprobante.

### Al crear un detalle

Se descuenta del stock la cantidad solicitada.

### Al aumentar una cantidad

Se valida que exista stock suficiente y se descuenta únicamente la diferencia.

Ejemplo:

```text
Stock disponible: 10
Cantidad anterior: 2
Cantidad nueva: 5

Diferencia: 3
```

Solo se descuentan **3 unidades adicionales**.

### Al disminuir una cantidad

Las unidades eliminadas se devuelven automáticamente al stock.

Ejemplo:

```text
Cantidad anterior: 5
Cantidad nueva: 2

Diferencia: -3
```

Se devuelven **3 unidades al stock**.

---

# 💰 Cálculo automático de Totales

El sistema calcula automáticamente el importe de cada detalle.

```text
Importe = Precio × Cantidad
```

El total del comprobante corresponde a la suma de todos sus detalles.

Cuando se modifica la cantidad de un detalle, el sistema calcula la diferencia entre el importe anterior y el nuevo importe.

De esta forma, el total del comprobante se actualiza automáticamente.

---

# 🔄 Actualización del Estado del Comprobante

Los comprobantes pueden cambiar de estado según el flujo de compra.

Los estados posibles son:

- NUEVO
- PENDIENTE
- PAGADO
- ENTREGADO
- CANCELADO

Dependiendo del estado, se restringen determinadas operaciones.

Los comprobantes en estado:

- PAGADO
- ENTREGADO
- CANCELADO

No permiten agregar ni modificar detalles.

Solo los comprobantes en estado:

- NUEVO
- PENDIENTE

Permiten modificaciones sobre sus productos.

---

# 📅 Obtención de Fecha mediante API Externa

Al crear un comprobante, la fecha se obtiene mediante el siguiente servicio REST externo:

`http://worldclockapi.com/api/json/utc/now`

La aplicación consume el servicio utilizando `RestTemplate`.

En caso de que el servicio externo no esté disponible o ocurra algún error durante la comunicación, el sistema utiliza como alternativa la fecha actual del sistema mediante la clase `Date` de Java.

```java
new Date()
```

Esto permite garantizar que la creación de comprobantes continúe funcionando incluso ante una falla del servicio externo.

---

# 🔍 Logging con AOP

El proyecto implementa **Aspect-Oriented Programming (AOP)** para realizar logging automático de las solicitudes realizadas a los Controllers.

El aspecto intercepta todos los métodos del paquete:

```text
com.example.zapatillasweb.controller
```

El Pointcut utilizado es:

```java
@Pointcut("execution(* com.example.zapatillasweb.controller.*.*(..))")
public void log(){}
```

La implementación permite registrar automáticamente:

- URL del request.
- Dirección IP del cliente.
- Clase y método ejecutado.
- Argumentos recibidos.
- Resultado devuelto.
- Tiempo de ejecución.

Para ello se utilizan los siguientes tipos de Advice:

### `@Before`

Registra la información antes de ejecutar el método.

### `@After`

Registra la finalización de la ejecución.

### `@AfterReturning`

Registra el resultado devuelto por el Controller.

### `@Around`

Calcula el tiempo total de ejecución de cada request.

Ejemplo de salida:

```text
------------INICIANDO-----------------------

Request : {
    url='http://localhost:8080/comprobantes',
    ip='127.0.0.1',
    classMethod='ComprobanteController.guardarComprobante'
}

Execution time: 25 ms

Result : ...

------------FINALIZADO-----------------------
```

El uso de AOP permite centralizar la lógica de logging sin necesidad de repetir código en cada Controller.

---

# 🔗 API REST

La aplicación expone endpoints REST para los principales recursos del sistema.

| Recurso | Descripción |
|---|---|
| `/clientes` | Gestión de clientes |
| `/productos` | Gestión de productos |
| `/stock-talles` | Gestión de stock por talle |
| `/comprobantes` | Gestión de comprobantes |
| `/comprobante-detalles` | Gestión de detalles de comprobantes |

Operaciones HTTP utilizadas:

- `GET` → Consultar información.
- `POST` → Crear nuevos registros.
- `PUT` → Actualizar registros existentes.
- `DELETE` → Eliminar registros.

---

# 🗄️ Base de Datos

El proyecto utiliza **H2 Database** como base de datos.

Configuración principal:

```properties
spring.datasource.url=jdbc:h2:file:./data/zapatillasweb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

La consola de H2 se encuentra disponible en:

```text
http://localhost:8080/h2-console
```

---

# ▶️ Ejecución del Proyecto

## Requisitos previos

Para ejecutar el proyecto es necesario contar con:

- Java 21 o superior.
- Maven.
- Git.

---

## Clonar el repositorio

```bash
git clone https://github.com/daparrei/zapatillasweb.git
```

---

## Ingresar al directorio

```bash
cd zapatillasweb
```

---

## Ejecutar la aplicación

```bash
mvn spring-boot:run
```

También es posible ejecutar la aplicación directamente desde un IDE compatible con Spring Boot, como:

- Visual Studio Code.

---

## Acceder a la aplicación

Una vez iniciada la aplicación, estará disponible en:

```text
http://localhost:8080
```

---

# 🎯 Conceptos aplicados

Durante el desarrollo del proyecto se aplicaron los siguientes conceptos:

- Programación Orientada a Objetos.
- Arquitectura en capas.
- API REST.
- Spring Boot.
- Spring Data JPA.
- Hibernate.
- Relaciones entre entidades.
- Persistencia de datos.
- Validaciones de negocio.
- Manejo de excepciones.
- Consumo de APIs externas.
- Uso de `RestTemplate`.
- Implementación de fallback ante errores.
- Gestión automática de stock.
- Cálculo automático de importes.
- Manejo de estados.
- Programación Orientada a Aspectos (AOP).
- Logging de requests.
- Medición de tiempo de ejecución.
- Control de versiones con Git.
- Repositorio remoto en GitHub.

---

# 👨‍💻 Autor

**Diego Parreira**

Proyecto desarrollado como entrega final del curso de Java.