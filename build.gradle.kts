import club.chachy.GitVersion
import org.bookmc.leather.tasks.GenerateJarTask

val branch = GitVersion(project.projectDir).branch

tasks.register<GenerateJarTask>("generateJar") {
    group = "leather"
    if (branch == "main") {
        error("The branch cannot be main! It must be a version")
    }

    version.set(branch)
    dest.set("mapping-utils")
}