package com.ssafy.hifes.ui.login

import NavigationItem
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ssafy.hifes.R
import com.ssafy.hifes.data.local.AppPreferences
import com.ssafy.hifes.ui.HifesDestinations
import com.ssafy.hifes.ui.theme.Grey
import com.ssafy.hifes.ui.theme.KakaoYellow
import com.ssafy.hifes.ui.theme.PrimaryPink
import com.ssafy.hifes.ui.theme.pretendardFamily
import kotlinx.coroutines.delay

private const val TAG = "LoginScreen_하이페스"

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    var isSplashFinished by remember { mutableStateOf(false) }
    val loginResponse by viewModel.loginResponse.observeAsState()

    LaunchedEffect(loginResponse?.result) {
        when (loginResponse?.result) {
            true -> {
                viewModel.saveFcmToken()
                navController.navigate(NavigationItem.Home.screenRoute) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }

            false -> {
                navController.navigate(HifesDestinations.LOGIN_DETAIL_ROUTE)
            }

            else -> {}
        }
    }

    var isLogin = false
    val jwtToken = AppPreferences.getAccessToken()
    if (!jwtToken.isNullOrEmpty() && AuthApiClient.instance.hasToken()) {
        isLogin = true

    }

    if (isLogin && isSplashFinished) {
        LaunchedEffect(Unit) {
            viewModel.saveFcmToken()
            navController.navigate(NavigationItem.Home.screenRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!isSplashFinished) {
            SplashScreen(onFinished = { isSplashFinished = true })
        }
        if (!isLogin && isSplashFinished){
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoAndTitle()
                Buttons(navController, viewModel, Modifier.padding(40.dp, 20.dp))
            }
        }

    }
}

@Composable
fun LogoAndTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.logo_hifes),
            contentDescription = "logo",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = stringResource(id = R.string.login_sub_title),
            fontFamily = pretendardFamily,
            fontWeight = FontWeight.Bold,
            color = Grey,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(id = R.string.login_title),
            fontFamily = pretendardFamily,
            fontWeight = FontWeight.Black,
            color = PrimaryPink,
            fontSize = 32.sp
        )

    }
}

@Composable
fun Buttons(navController: NavController, viewModel: LoginViewModel, modifier: Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
    ) {
        LoginButton(
            color = KakaoYellow,
            title = stringResource(R.string.kakao_login),
            onClick = {
                login(navController, viewModel, context)

            },
            textColor = R.color.black
        )

    }
}

private fun login(navController: NavController, viewModel: LoginViewModel, context: Context) {
    // 카카오계정으로 로그인 공통 callback 구성
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("카카오계정으로 로그인 실패", error.toString())
        } else if (token != null) {
            Log.i("카카오계정으로 로그인 성공 ${token.accessToken}", token.accessToken)
            // jwt 토큰 발급 & 유저 정보
            viewModel.login(token.accessToken)
        }
    }
    // 카카오계정으로 로그인
    UserApiClient.instance.loginWithKakaoAccount(
        context,
        callback = callback
    )

}

@Preview
@Composable
fun LoginScreenPrev() {
}