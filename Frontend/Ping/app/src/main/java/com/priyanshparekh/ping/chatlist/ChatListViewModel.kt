package com.priyanshparekh.ping.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshparekh.ping.chatlist.dto.GetChatResponse
import com.priyanshparekh.ping.network.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel : ViewModel() {

    val chatListRepository = ChatListRepository()

    private val _chatListUiState: MutableStateFlow<ChatListUiState> = MutableStateFlow(ChatListUiState())
    val chatListUiState: StateFlow<ChatListUiState> = _chatListUiState

    private val _unfilteredChatList: MutableStateFlow<List<GetChatResponse>> = MutableStateFlow(listOf())

    fun getChatList() {
        viewModelScope.launch {
            val response = chatListRepository.getChatList()

            when (response) {
                is ApiResponse.SUCCESS -> {
                    _chatListUiState.update {
                        it.copy(chatList = response.data)
                    }
                    _unfilteredChatList.value = response.data
                }
                is ApiResponse.ERROR -> {
                    _chatListUiState.update {
                        it.copy(errorMessage = response.message)
                    }
                }
            }
        }
    }

    fun searchInputChange(input: String) {
        val unfilteredList = _unfilteredChatList.value
        val filteredChatList = if (input.isBlank()) {
            unfilteredList
        } else {
            unfilteredList.filter {
                it.name.contains(input, ignoreCase = true)
            }
        }
        _chatListUiState.update {
            it.copy(searchInput = input, chatList = filteredChatList)
        }
    }

}