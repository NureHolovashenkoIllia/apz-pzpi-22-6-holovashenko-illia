@Composable
fun UserScreen(userStatus: String, modifier: Modifier = Modifier) {
    when (userStatus) {
        "guest" -> {
            Text("Ласкаво просимо! Будь ласка, увійдіть у систему.",
                modifier = modifier)
        }
        "user" -> {
            Text("Вітаємо вас у вашому профілі.",
                modifier = modifier)
        }
        "admin" -> {
            Text("Доступ до панелі адміністратора.",
                modifier = modifier)
        }
        else -> {
            Text("Невідомий статус користувача.",
                modifier = modifier)
        }
    }
}

interface UserState {
    @Composable
    fun RenderScreen()
}

class GuestState : UserState {
    @Composable
    override fun RenderScreen() {
        Text("Ласкаво просимо! Будь ласка, увійдіть у систему.")
    }
}

class UserStateImpl : UserState {
    @Composable
    override fun RenderScreen() {
        Text("Вітаємо вас у вашому профілі.")
    }
}

class AdminState : UserState {
    @Composable
    override fun RenderScreen() {
        Text("Доступ до панелі адміністратора.")
    }
}

class UnknownState : UserState {
    @Composable
    override fun RenderScreen() {
        Text("Невідомий статус користувача.")
    }
}

class UserContext(status: String) {
    val userState: UserState = when (status) {
        "guest" -> GuestState()
        "user" -> UserStateImpl()
        "admin" -> AdminState()
        else -> UnknownState()
    }
}

@Composable
fun UserScreen(userStatus: String) {
    val context = UserContext(userStatus)
    context.userState.RenderScreen()
}