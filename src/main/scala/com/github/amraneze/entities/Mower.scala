package com.github.amraneze.entities

import com.github.amraneze.entities.Movement.Coordination

/**
 * A case class to create a new mower entity
 * @param id the id of the mower
 * @param coordination its coordination (x, y)
 * @param commands the list of commands that the mower should execute after running
 */
case class Mower(id: String, coordination: Coordination, commands: Seq[Char])
