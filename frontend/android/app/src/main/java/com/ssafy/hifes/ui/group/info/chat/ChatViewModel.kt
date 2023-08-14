package com.ssafy.hifes.ui.group.info.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ssafy.hifes.data.model.MessageData
import com.ssafy.hifes.data.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val TAG = "ChatViewModel_하이페스"

class ChatViewModel : ViewModel() {
    val gson = Gson()
    private val firebaseDB = Firebase.database

    var chatMessages = MutableStateFlow<List<MessageData>>(emptyList())

    //    var chatData = MutableStateFlow<ChatData?>(null)
    var userId = 29

    // 채팅방 들어가자마자 조회 - 한번도 채팅하지 않은 경우(채팅방이 생성되어있지 않은 경우) 조회불가
    fun enterChatRoom(groupId: String) {
        // 한번도 채팅하지 않은경우는 조회 불가
        Log.d("채팅방 id", groupId)
        firebaseDB.reference.child("chat").child(groupId).get()
            .addOnSuccessListener {
                Log.d("채팅방 정보", it.value.toString())
//                // 데이터는 hashMap 형태로 오기때문에 객체 형태로 변환해줘야함
//                Log.d(TAG, "enterChatRoom: ")
//                val messageData = gson.fromJson(it.value.toString(), MessageData::class.java)
//                Log.d(TAG, "enterChatRoom: $messageData")
            }
            .addOnFailureListener {
                Log.d("채팅룸 정보 가져오기 실패", it.toString())
            }

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val _chatMessage = arrayListOf<MessageData>()
                val messageDataList = snapshot.value as ArrayList<HashMap<String, Any>>?
                Log.d(TAG, "onDataChange (snapshot): $snapshot")
                Log.d(TAG, "onDataChange: $messageDataList")

                // snapshot은 hashMap 형태로 오기때문에 객체 형태로 변환해줘야함
                messageDataList?.forEach { messageMap ->
                    val userDataMap = messageMap["userData"] as? Map<*, *>
                    val nickname = userDataMap?.get("nickname") as? String
                    val id = userDataMap?.get("id") as? String
                    val time = messageMap["time"] as? Long
                    val content = messageMap["content"] as? String

                    if (nickname != null && id != null && time != null && content != null) {
                        val userData = UserData(nickname, id)
                        val messageData = MessageData(content, userData, time)
                        Log.d(TAG, "onDataChange: $messageData")
                        _chatMessage.add(messageData)
                    } else {
                        Log.e(TAG, "Error parsing the message map: $messageMap")
                    }
                }


                chatMessages.value = arrayListOf()
                chatMessages.value = _chatMessage.toList()
                Log.d("변화 리스너2", chatMessages.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        firebaseDB.reference.child("chat").child(groupId).child("messages")
            .addValueEventListener(chatListener)
    }

    // 메세지 보내기
    fun newMessageTest(chatId: String, messageData: MessageData) {
        if (chatMessages.value.isEmpty()) {
            chatMessages.value = listOf(messageData)
            // 첫 메세지일때 채팅방 생성 - 채팅룸이 생성되는 시점
//            newChatRoom(chatId, userId, chatMessages.value)
        } else {
            chatMessages.value += messageData
            firebaseDB.reference.child("chat").child(chatId).child("messages")
                .setValue(chatMessages.value)
                .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "메세지 보내기 성공")
                }
                .addOnFailureListener {
                    Log.d("메세지 보내기 실패", it.toString())
                }
        }
    }

    fun sendNewMessage(groupId: String, messageData: MessageData) {

        Log.d(TAG, "testNewMessage: chatMeesages -> ${chatMessages.value}")
        chatMessages.value += messageData
        Log.d(TAG, "testNewMessage: chatMeesages -> ${chatMessages.value}")

        firebaseDB.reference.child("chat").child(groupId).child("messages")
            .setValue(chatMessages.value)
            .addOnSuccessListener {
                Log.d("newChatRoomSuccess", "메세지 보내기 성공 ${chatMessages.value}")
            }
            .addOnFailureListener {
                Log.d("메세지 보내기 실패", it.toString())
            }

    }

    // 채팅룸 생성
    private fun newChatRoom(
        groupId: String,
        message: List<MessageData>
    ) {
        viewModelScope.launch {
//            val chatData = ChatData(chatId, message)

            firebaseDB.reference.child("chat").child(groupId).setValue(message)
                .addOnSuccessListener {
                    Log.d("newChatRoomSuccess", "채팅룸 생성 완료")
                    // 생성시 채팅 listener 재호출
                    enterChatRoom(groupId)
                }
                .addOnFailureListener {
                    Log.d("채팅룸 생성 실패", it.toString())
                }


        }
    }
}