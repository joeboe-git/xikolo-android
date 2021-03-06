dependencies {
    val implementation by configurations
    val kapt by configurations
    val androidTestImplementation by configurations
    val testImplementation by configurations

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}")

    val kotlinCoroutinesVersion = "1.3.7"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")

    // kotlin extensions
    implementation("ru.gildor.coroutines:kotlin-coroutines-retrofit:1.1.0")

    // networking
    implementation(SharedDependencies.OK_HTTP)
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP}")

    implementation("com.coolerfall:android-http-download-manager:1.6.2")

    // images
    val glideVersion = "4.11.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    // type-safe http api
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

    // json
    implementation(SharedDependencies.GSON)
    implementation("com.squareup.moshi:moshi:1.9.2")

    // json:api
    val moshijsonapiVersion = "3.5.0"
    implementation("moe.banana:moshi-jsonapi:$moshijsonapiVersion")
    implementation("moe.banana:moshi-jsonapi-retrofit-converter:$moshijsonapiVersion")

    // user interface
    implementation("de.psdev.licensesdialog:licensesdialog:2.1.0")
    implementation("com.github.Commit451:bypasses:1.1.0")
    implementation("com.github.rubensousa:previewseekbar:3.0.0")

    // view binding
    val butterknifeVersion = "10.2.1"
    implementation("com.jakewharton:butterknife:$butterknifeVersion")
    kapt("com.jakewharton:butterknife-compiler:$butterknifeVersion")

    // field binding
    val autobundleVersion = "5.0.2"
    implementation("com.github.yatatsu:autobundle:$autobundleVersion")
    kapt("com.github.yatatsu:autobundle-processor:$autobundleVersion")

    // video
    implementation("com.google.android.exoplayer:exoplayer:2.11.4")

    // play services
    implementation("com.google.android.gms:play-services-cast-framework:18.1.0")
    implementation("com.google.android.gms:play-services-gcm:17.0.0")

    // firebase
    implementation("com.google.firebase:firebase-analytics:17.4.2")
    implementation("com.google.firebase:firebase-crashlytics:17.0.0")

    // material design
    implementation("com.google.android.material:material:1.1.0")

    // androidx
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    implementation("androidx.mediarouter:mediarouter:1.1.0")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.browser:browser:1.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.work:work-runtime-ktx:2.3.4")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    // local libraries
    implementation(project(":lanalytics"))

    // testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    testImplementation("junit:junit:4.13")
}
