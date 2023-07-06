package eom.tri.weather.controller

import eom.tri.weather.exception.HttpError
import eom.tri.weather.exception.InvalidInputException
import eom.tri.weather.exception.NotFoundException
import eom.tri.weather.service.RequestService
import org.apache.logging.log4j.core.util.SystemMillisClock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange

@RestControllerAdvice
class GlobalExceptionHandler(
    private val requestService : RequestService,
    ) {
    private val logger: Logger= LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    @ResponseBody
    fun handleNotFoundException(exchange: ServerWebExchange, ex: Exception): HttpError =
        createHttpError(HttpStatus.NOT_FOUND, exchange.request, exchange.response, ex)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException::class)
    @ResponseBody fun handleInvalidInputException(exchange: ServerWebExchange, ex: Exception): HttpError =
        createHttpError(HttpStatus.UNPROCESSABLE_ENTITY, exchange.request, exchange.response, ex)

    private fun createHttpError(status: HttpStatus, request: ServerHttpRequest, response: ServerHttpResponse, ex: Exception): HttpError {
        val path = request.path.pathWithinApplication().value()
        var message = ex.localizedMessage
        val requestId = request.id
        val clock = SystemMillisClock().currentTimeMillis()

        val requestLogMap = mutableMapOf(
            "requestId" to requestId,
            "path" to path,
            "status" to status.toString(),
            "created" to clock.toString(),
            "message" to message,
        )

        requestService.saveRequestLog(requestLogMap).subscribe()

        logger.debug("Returning HTTP status: {} for path: {}, message: {}, cause:{}", status, path, message,ex.cause)
        response.statusCode = status
        return HttpError(id = requestId, path, status, message)
    }
}
