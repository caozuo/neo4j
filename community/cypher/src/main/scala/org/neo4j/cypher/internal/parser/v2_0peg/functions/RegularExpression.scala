/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.parser.v2_0peg.functions

import org.neo4j.cypher.internal.parser.v2_0peg._
import org.neo4j.cypher.internal.symbols._
import org.neo4j.cypher.internal.commands
import org.neo4j.cypher.internal.commands.{expressions => commandexpressions}

case class RegularExpression extends Function with LegacyPredicate {
  def name = "=~"

  def semanticCheck(ctx: ast.Expression.SemanticContext, invocation: ast.FunctionInvocation) : SemanticCheck = {
    checkArgs(invocation, 2) >>=
    invocation.arguments.limitType(StringType()) >>=
    invocation.limitType(BooleanType())
  }

  def toCommand(invocation: ast.FunctionInvocation) = {
    val left = invocation.arguments(0)
    val right = invocation.arguments(1)
    nullable(invocation, right.toCommand match {
      case literal: commandexpressions.Literal => commands.LiteralRegularExpression(left.toCommand, literal)
      case command => commands.RegularExpression(left.toCommand, command)
    })
  }
}