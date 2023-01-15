import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.infrastructure.services.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController

@RequestMapping("/api")
class PaymentController(
    @Autowired
    private val paymentService: PaymentService,
) {
    @PostMapping("/payments/markPaid", "application/json")
    fun markAsPaid(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.markAsPaid(applicationId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/payments/save", "application/json")
    fun save(@RequestBody payment: JpaPayment): ResponseEntity<*>? {
        val result = paymentService.save(payment)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/payments/byApplication", "application/json")
    fun findByApplicationId(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.findByApplicationId(applicationId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/payments/byId", "application/json")
    fun findById(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.findById(applicationId)
        return ResponseEntity.ok(result)
    }
}

