package com.socurites.dl4j.vgg16.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Map;

import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SampleController {
	private static final String UPLOAD_DIR = "/tmp";

	@GetMapping("/")
	public String welcome(Map<String, Object> model) {
		return "index";
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) throws Exception {

		try {
			String fileName = uploadfile.getOriginalFilename();
			String filepath = Paths.get(UPLOAD_DIR, fileName).toString();

			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/infer", method = RequestMethod.GET)
	@ResponseBody
	public String infer(@RequestParam("fileName") String fileName) throws Exception {
        File file = new File(Paths.get(UPLOAD_DIR, fileName).toString());

        // Convert file to INDArray
        NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
        INDArray image = loader.asMatrix(file);

        // delete the physical file, if left our drive would fill up over time
        file.delete();

        // Mean subtraction pre-processing step for VGG
        DataNormalization scaler = new VGG16ImagePreProcessor();
        scaler.transform(image);

        //Inference returns array of INDArray, index[0] has the predictions
        INDArray[] output = vgg16.output(false,image);

        // convert 1000 length numeric index of probabilities per label
        // to sorted return top 5 convert to string using helper function VGG16.decodePredictions
        // "predictions" is string of our results
        String predictions = TrainedModels.VGG16.decodePredictions(output[0]);

        // return results along with form to run another inference
        return "<h4> '" + predictions  + "' </h4>" +
                "Would you like to try another" +
                form;
        //return "<h1>Your image is: '" + tempFile.getName(1).toString() + "' </h1>";

	}
}