package com.priyanshparekh.ping.addchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshparekh.ping.addchat.dto.AddChatRequest
import com.priyanshparekh.ping.network.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddChatViewModel: ViewModel() {

    val addChatRepository = AddChatRepository()

    private val _addChatUiState: MutableStateFlow<AddChatUiState> = MutableStateFlow(AddChatUiState())
    val addChatUiState: StateFlow<AddChatUiState> = _addChatUiState

    fun onQueryChange(query: String) {
        _addChatUiState.update {
            it.copy(query = query)
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            val response = addChatRepository.search(query)

            when (response) {
                is ApiResponse.SUCCESS -> {
                    _addChatUiState.update {
                        it.copy(searchResult = response.data, errorMessage = null)
                    }
                }
                is ApiResponse.ERROR -> {
                    _addChatUiState.update {
                        it.copy(errorMessage = response.message)
                    }
                }
            }
        }
    }

    fun filter(query: String) {
        viewModelScope.launch {
            addChatUiState.value.searchResult.filter { it.name.contains(query) }
        }
    }

    fun addNewChat(userId: Long) {
        viewModelScope.launch {
            val response = addChatRepository.addNewChat(AddChatRequest(userId))

            when (response) {
                is ApiResponse.SUCCESS -> {
                    _addChatUiState.update {
                        it.copy(errorMessage = null)
                    }
                }
                is ApiResponse.ERROR -> {
                    _addChatUiState.update {
                        it.copy(errorMessage = response.message)
                    }
                }
            }
        }
    }

}