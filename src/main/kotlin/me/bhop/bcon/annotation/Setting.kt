package me.bhop.bcon.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Setting(val key: String = "", val comments: Array<String> = [])