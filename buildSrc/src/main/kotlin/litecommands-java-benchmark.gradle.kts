plugins {
    id("java-library")
    id("me.champeau.jmh")
}

sourceSets.getByName("jmh") {
    java.setSrcDirs(listOf("benchmark"))
    resources.setSrcDirs(emptyList<String>())
}

dependencies {
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
}