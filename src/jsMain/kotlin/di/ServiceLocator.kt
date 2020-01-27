package di

import model.repo.TimeRepo
import model.repo.TimeRepoRemote

object ServiceLocator {

    val timeRepo: TimeRepo by lazy { TimeRepoRemote() }
}