package com.yashishu.bsa.ui.user


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.yashishu.bsa.R
import com.yashishu.bsa.auth.Customer

class PaymentFragment : Fragment() {
    private var _binding: com.yashishu.bsa.databinding.FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private lateinit var paymentIntentClientSecret: String

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        paymentIntentClientSecret = getString(R.string.pk)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAccount(db, auth)
        binding.payButton.isEnabled = false // Disable the button until we have a payment intent
        viewModel.cartTotal.observe(viewLifecycleOwner) {
            binding.tvTotalAmount.text = "₹$it"
        }
        viewModel.customer.observe(viewLifecycleOwner) {
            validateFromServer(it, viewModel.cartTotal.value?.toFloat() ?: 0f)
        }
        binding.payButton.setOnClickListener {
            presentPaymentSheet()
        }

    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                showAlert("Payment cancelled")
                binding.payButton.isEnabled = true
            }
            is PaymentSheetResult.Failed -> {
                showAlert("Payment failed ${paymentSheetResult.error.message}")
            }
            is PaymentSheetResult.Completed -> {
                showAlert("Payment completed successfully")
                viewModel.saveOrder(db, auth)
                binding.payButton.visibility = View.GONE
            }
        }
    }

    private fun validateFromServer(customer: Customer, amount: Float) {
        val url = getString(R.string.api_url)
        val params = listOf(
            "amount" to amount * 100,
            "currency" to "inr",
            "email" to customer.email,
            "name" to customer.name,
            "phone" to customer.phone,
            "id" to customer.uid
        )
        url.httpPost(
            params
        ).responseJson { req, res, result ->
            Log.d("Request", req.toString())
            Log.d("Response", res.toString())
            Log.d("Result", result.toString())
            if (result is Result.Success) {
                val responseJson = result.get().obj()
                paymentIntentClientSecret = responseJson.getString("paymentIntent")
                val customerId = responseJson.getString("customer")
                val ephemeralKeySecret = responseJson.getString("ephemeralKey")
                customerConfig = PaymentSheet.CustomerConfiguration(customerId, ephemeralKeySecret)
                val publishableKey = responseJson.getString("publishableKey")
                PaymentConfiguration.init(requireContext(), publishableKey)
                presentPaymentSheet()
            } else {
                showAlert("Error validating from server, make sure server is running on the network and update the API URL in strings.xml")
            }
        }

    }

    private fun presentPaymentSheet() {

        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret, PaymentSheet.Configuration(
                merchantDisplayName = "My Business Merch",
                customer = customerConfig,
                allowsDelayedPaymentMethods = true
            )
        )
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext()).setTitle("Alert").setMessage(message)
            .setPositiveButton("OK", null).show()
    }

}