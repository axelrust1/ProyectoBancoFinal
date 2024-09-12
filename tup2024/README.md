# tup2024

Para correr la app: mvn spring-boot:run

Tecnologías
Java
Springboot
Junit + Mockito

POST /cliente
{
    "nombre" : "String",
    "apellido" : "String",
    "fechaNacimiento" : "String", //anio-mes-dia
    "banco" : "String",
    "tipoPersona" : "String", //"F" o "J"
    "dni" : long
}
GET /cliente/{dni} //obtiene el cliente en el siguiente formato
{
    "nombre" : "String",
    "apellido" : "String",
    "fechaNacimiento" : "String",
    "banco" : "String",
    "tipoPersona" : "String",
    "dni" : long
}

POST /cuenta
{
    "dniTitular": 0,
    "tipoCuenta": "string", // "C" o "A" CUENTA CORRIENTE O CAJA DE AHORRO
    "moneda": "string" // "P" o "D" PESOS O DOLARES
}

GET /cuenta/{numeroCuenta} //obtiene la cuenta en el siguiente formato
{
    "numeroCuenta": long,
    "fechaCreacion": "String",
    "balance": double,
    "tipoCuenta": "String",
    "titular": long,
    "moneda": "String",
    "movimientos": [(Lista de movimientos de la cuenta)]
}

GET /cuenta/{numeroCuenta}/transacciones //obtiene todos los movimientos de esa cuenta en este formato
{
    "numeroCuenta": long,
    "transacciones": [
        {
            "fecha": "String",
            "tipo": "String", //indica si es es debito o credito
            "descripcionBreve": "String", //indica si es transferencia, deposito o retiro
            "monto": Double
        }
    ]
}

POST /api/transfer //realiza la transferencia
{
    "cuentaOrigen":long,
    "cuentaDestino":long,
    "monto": double,
    "moneda" : "String" PESOS O DOLARES
}

POST /api/deposito //realiza deposito
{
    "cuenta":long,
    "monto": double,
    "moneda" : "String" PESOS O DOLARES
}

POST /api/retiro //realiza retiro
{
    "cuenta":long,
    "monto": double,
    "moneda" : "String" PESOS O DOLARES
}