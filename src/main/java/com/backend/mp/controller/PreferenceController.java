package com.backend.mp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.backend.mp.converter.PreferenceConverter;
import com.backend.mp.dto.PreferenceDTO;
import com.backend.mp.entity.Preference;
import com.backend.mp.util.WrapperResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PreferenceController {

	@Autowired
	private PreferenceConverter preferenceConverter;

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${config.url}")
	private String url;

	
	private ResponseEntity<WrapperResponse<PreferenceDTO>> getPreference(String img, String title, double price,
			int unit, String device_id) {
		
		img = img.substring(1);

		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "https://api.mercadopago.com/checkout/preferences";
		final ObjectMapper objectMapper = new ObjectMapper();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer APP_USR-6317427424180639-042414-47e969706991d3a442922b0702a0da44-469485398");
		headers.set("x-integrator-id", "dev_24c65fb163bf11ea96500242ac130004");
		headers.set("X-meli-session-id", device_id);
				

		JSONArray items = new JSONArray();
		JSONObject item = new JSONObject();
		JSONObject phone = new JSONObject();
		JSONObject payer = new JSONObject();
		JSONObject address = new JSONObject();
		JSONObject backUrls = new JSONObject();
		JSONObject preference = new JSONObject();
		JSONObject paymentMethods = new JSONObject();
		JSONArray excludedPaymentTypes = new JSONArray();
		JSONObject excludedPaymentType = new JSONObject();
		JSONArray excludedPaymentMethods = new JSONArray();
		JSONObject excludedPaymentMethod = new JSONObject();

		Preference pref = new Preference();

		// create item
		try {
			item.put("title", title);
			item.put("description", "Dispositivo móvil de Tienda e-commerce");
			item.put("picture_url", url+img);
			item.put("category_id", "CAT1234");
			item.put("id", "1234");
			item.put("quantity", unit);
			item.put("unit_price", price);
			// add item to array
			items.put(item);
			// add items to preference
			preference.put("items", items);
			// create payer
			payer.put("name", "Lalo");
			payer.put("surname", "Landa");
			payer.put("email", "test_user_63274575@testuser.com");
			// create phone
			phone.put("area_code", "11");
			phone.put("number", "22223333");
			payer.put("phone", phone);
			// create address
			address.put("zip_code", "1111");
			address.put("street_name", "False");
			address.put("street_number", "123");
			payer.put("address", address);
			// add payer to preference
			preference.put("payer", payer);
			// create payment
			paymentMethods.put("installments", 6);
			paymentMethods.put("default_installments", 6);
			// create excluded payments methods
			excludedPaymentMethod.put("id", "amex");
			// add payment method to array
			excludedPaymentMethods.put(excludedPaymentMethod);
			// create payment methods type
			excludedPaymentType.put("id", "atm");
			// add payment method type to array
			excludedPaymentTypes.put(excludedPaymentType);
			// create succes url
			backUrls.put("success", url+"/success");
			backUrls.put("pending", url+"/pending");
			backUrls.put("failure", url+"/failure");
			// add back_urls
			preference.put("back_urls", backUrls);
			paymentMethods.put("excluded_payment_methods", excludedPaymentMethods);
			paymentMethods.put("excluded_payment_types", excludedPaymentTypes);
			// add payment methods to preference
			preference.put("payment_methods", paymentMethods);
			preference.put("notification_url", "https://backend-mp.herokuapp.com/notification/create?source_news=webhooks");
			preference.put("auto_return", "approved");
			preference.put("external_reference", "clobesney1@gmail.com");

			log.info(preference.toString());

			HttpEntity<String> request = new HttpEntity<String>(preference.toString(), headers);

			ResponseEntity<String> result = restTemplate.postForEntity(baseUrl, request, String.class);

			JsonNode root = objectMapper.readTree(result.getBody());
			if (result.getStatusCodeValue() != 201) {
				String message = root.get("message").textValue();
				log.error(message);
				return new WrapperResponse<PreferenceDTO>(true, message, null)
						.createResponse(result.getStatusCode());
			}
			log.info("RESPONSE: "+root.textValue());
			JsonNode id = root.get("id");
			log.info("ID_REFRENCE "+id.textValue());
			JsonNode collector_id = root.get("collector_id");
			JsonNode itemsNode = root.get("items");
			JsonNode init_point = root.get("init_point");

			pref.setId(id.textValue());
			pref.setCollector_id(collector_id.longValue());
			pref.setInit_point(init_point.textValue());
			pref.setItems(objectMapper.convertValue(itemsNode, ArrayList.class));

		} catch (JSONException | JsonProcessingException e) {
			e.printStackTrace();
		}

		WrapperResponse<PreferenceDTO> response = new WrapperResponse<PreferenceDTO>(true, "Ok.",
				preferenceConverter.fromEntity(pref));
		return response.createResponse(HttpStatus.OK);
	}

	@GetMapping("/detail")
	public String getDetail(@RequestParam(name = "img", required = true) String img,
			@RequestParam(name = "title", required = true) String title,
			@RequestParam(name = "price", required = true) int price,
			@RequestParam(name = "unit", required = true) int unit, 
			@RequestParam(name = "deviceId", required = false) String deviceId,
			 Model model) {
		model.addAttribute("img", img);
		model.addAttribute("title", title);
		model.addAttribute("price", price);
		model.addAttribute("unit", unit);
		
		ResponseEntity<WrapperResponse<PreferenceDTO>> res = getPreference(img, title, price, unit, deviceId);
		model.addAttribute("id", res.getBody().getBody().getId());
		return "detail";
	}
	
	@GetMapping("/success")
	public String success(@RequestParam(name = "collection_id", required = false) String collection_id,
			@RequestParam(name = "collection_status", required = false) String collection_status,
			@RequestParam(name = "external_refence", required = false) String external_refence,
			@RequestParam(name = "payment_type", required = false) String payment_type,
			@RequestParam(name = "preference_id", required = false) String preference_id,
			@RequestParam(name = "site_id", required = false) String site_id,
			@RequestParam(name = "processing_mode", required = false) String processing_mode,
			@RequestParam(name = "merchant_account_id", required = false) String merchant_account_id, Model model) {

		model.addAttribute("collection_id", collection_id);
		model.addAttribute("collection_status", collection_status);
		model.addAttribute("external_refence", external_refence);
		model.addAttribute("payment_type", payment_type);
		model.addAttribute("preference_id", preference_id);
		model.addAttribute("site_id", site_id);
		model.addAttribute("processing_mode", processing_mode);
		model.addAttribute("merchant_account_id", merchant_account_id);

		return "success";
	}

	@GetMapping("/failure")
	public String failure(@RequestParam(name = "collection_id", required = false) String collection_id,
			@RequestParam(name = "collection_status", required = false) String collection_status,
			@RequestParam(name = "external_refence", required = false) String external_refence,
			@RequestParam(name = "payment_type", required = false) String payment_type,
			@RequestParam(name = "preference_id", required = false) String preference_id,
			@RequestParam(name = "site_id", required = false) String site_id,
			@RequestParam(name = "processing_mode", required = false) String processing_mode,
			@RequestParam(name = "merchant_account_id", required = false) String merchant_account_id, Model model) {
		
		model.addAttribute("collection_id", collection_id);
		model.addAttribute("collection_status", collection_status);
		model.addAttribute("external_refence", external_refence);
		model.addAttribute("payment_type", payment_type);
		model.addAttribute("preference_id", preference_id);
		model.addAttribute("site_id", site_id);
		model.addAttribute("processing_mode", processing_mode);
		model.addAttribute("merchant_account_id", merchant_account_id);

		return "failure";
	}

	@GetMapping("/pending")
	public String pending(@RequestParam(name = "collection_id", required = false) String collection_id,
			@RequestParam(name = "collection_status", required = false) String collection_status,
			@RequestParam(name = "external_refence", required = false) String external_refence,
			@RequestParam(name = "payment_type", required = false) String payment_type,
			@RequestParam(name = "preference_id", required = false) String preference_id,
			@RequestParam(name = "site_id", required = false) String site_id,
			@RequestParam(name = "processing_mode", required = false) String processing_mode,
			@RequestParam(name = "merchant_account_id", required = false) String merchant_account_id, Model model) {
		
		model.addAttribute("collection_id", collection_id);
		model.addAttribute("collection_status", collection_status);
		model.addAttribute("external_refence", external_refence);
		model.addAttribute("payment_type", payment_type);
		model.addAttribute("preference_id", preference_id);
		model.addAttribute("site_id", site_id);
		model.addAttribute("processing_mode", processing_mode);
		model.addAttribute("merchant_account_id", merchant_account_id);

		return "pending";
	}

}
