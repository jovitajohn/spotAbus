/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.core.examples.kotlin.ml.classification

import android.app.Activity
import android.graphics.Point
import android.media.Image
import android.util.Log
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.ar.core.examples.kotlin.ml.classification.utils.ImageUtils
import com.google.ar.core.examples.kotlin.ml.classification.utils.VertexUtils.rotateCoordinates
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.tasks.await

/** Analyzes an image using ML Kit. */
class MLKitObjectDetector(context: Activity) : ObjectDetector(context) {
  // To use a custom model, follow steps on
  // https://developers.google.com/ml-kit/vision/object-detection/custom-models/android.
   val model = LocalModel.Builder().setAssetFilePath("model.tflite").build()
   val builder = CustomObjectDetectorOptions.Builder(model)

  // For the ML Kit default model, use the following:
 // val builder = ObjectDetectorOptions.Builder()

  private val options =
    builder
      .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
      .enableClassification()
      .enableMultipleObjects()
      .build()
  private val detector = ObjectDetection.getClient(options)

  override suspend fun analyze(image: Image, imageRotation: Int): List<DetectedObjectResult> {
    // `image` is in YUV
    // (https://developers.google.com/ar/reference/java/com/google/ar/core/Frame#acquireCameraImage()),
    val convertYuv = convertYuv(image)

    // The model performs best on upright images, so rotate it.
    val rotatedImage = ImageUtils.rotateBitmap(convertYuv, imageRotation)

    val inputImage = InputImage.fromBitmap(rotatedImage, 0)

    val mlKitDetectedObjects = detector.process(inputImage).await()
    return mlKitDetectedObjects.mapNotNull { obj ->
      val bestLabel = obj.labels.maxByOrNull { label -> label.confidence } ?: return@mapNotNull null
      val coords =
        Point(obj.boundingBox.exactCenterX().toInt(), obj.boundingBox.exactCenterY().toInt())
      val rotatedCoordinates =
        coords.rotateCoordinates(rotatedImage.width, rotatedImage.height, imageRotation)

      //for QR reading
      var qr = getBarcodeValue(image)

     // DetectedObjectResult(bestLabel.confidence, bestLabel.text, rotatedCoordinates)
      DetectedObjectResult(bestLabel.confidence, qr, rotatedCoordinates)
    }
  }

  fun getBarcodeValue(image: Image): String{
     var qrValue :String = ""
    var barcodeDetector: BarcodeDetector =
      BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.ALL_FORMATS).build()

    try {

      val bitmap = convertYuv(image)
      if (barcodeDetector!!.isOperational && bitmap != null) {
        val frame = Frame.Builder().setBitmap(bitmap).build()
        val barcodes = barcodeDetector!!.detect(frame)
        for (index in 0 until barcodes.size()) {
          val code = barcodes.valueAt(index)
          qrValue = code.rawValue
          break

        }

      }
    } catch (e: Exception) {
      Log.e("Ar screen qr detection","Couldn't load image")
    }
    return qrValue
  }

  @Suppress("USELESS_IS_CHECK")
  fun hasCustomModel() = builder is CustomObjectDetectorOptions.Builder
}
