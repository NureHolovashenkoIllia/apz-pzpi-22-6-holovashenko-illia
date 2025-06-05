package ua.nure.holovashenko.flameguard_mobile.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.nure.holovashenko.flameguard_mobile.data.local.TokenDataStore
import ua.nure.holovashenko.flameguard_mobile.data.remote.AuthApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.AuthInterceptor
import ua.nure.holovashenko.flameguard_mobile.data.remote.BuildingApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.MeasurementApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.SensorApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.UserApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenDataStore: TokenDataStore): AuthInterceptor {
        return AuthInterceptor(tokenDataStore)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideBuildingApi(retrofit: Retrofit): BuildingApi =
        retrofit.create(BuildingApi::class.java)

    @Provides
    @Singleton
    fun provideSensorApi(retrofit: Retrofit): SensorApi =
        retrofit.create(SensorApi::class.java)

    @Provides
    @Singleton
    fun provideMeasurementApi(retrofit: Retrofit): MeasurementApi =
        retrofit.create(MeasurementApi::class.java)
}
