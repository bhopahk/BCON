package me.bhop.bcon

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BconSerializable(val fileName: String, val header: String = "", val footer: String = "") //todo these values are not implemented, also they will only be used if the annotated class is being mapped to an orphannode (ie the root of the file)