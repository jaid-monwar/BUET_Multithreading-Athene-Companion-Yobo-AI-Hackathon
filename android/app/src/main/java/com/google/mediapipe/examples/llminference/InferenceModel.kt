package com.google.mediapipe.examples.llminference

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import java.io.File
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class InferenceModel private constructor(context: Context, modelName: String) {
    private var llmInference: LlmInference
    private val modelPath = "/data/local/tmp/llm/$modelName"

    private val _partialResults = MutableSharedFlow<Pair<String, Boolean>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val partialResults: SharedFlow<Pair<String, Boolean>> = _partialResults.asSharedFlow()

    init {
        if (!File(modelPath).exists()) {
            throw IllegalArgumentException("Model not found at path: $modelPath")
        }

        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(1024)
            .setResultListener { partialResult, done ->
                _partialResults.tryEmit(partialResult to done)
            }
            .build()

        llmInference = LlmInference.createFromOptions(context, options)
    }

    fun generateResponseAsync(prompt: String) {
        llmInference.generateResponseAsync(prompt)
    }

    companion object {
        private var instances: MutableMap<String, InferenceModel> = mutableMapOf()

        fun listAvailableModels(): List<String> {
            val modelsDir = File("/data/local/tmp/llm/")

            return modelsDir.listFiles()?.mapNotNull { it.takeIf(File::isFile)?.name } ?: emptyList()
        }

        fun getInstance(context: Context, modelName: String): InferenceModel {
            return instances.getOrPut(modelName) {
                InferenceModel(context.applicationContext, modelName)
            }
        }
    }
}


