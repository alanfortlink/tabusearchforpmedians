package default

/**
 * Created by alanfortlink on 5/30/17.
 */
class Solution{
    var facilities = mutableListOf<Node>()
    var cost: Double = Double.MAX_VALUE
    var problem = Problem()
    var mapToNodes = mutableListOf<Node>()

    constructor(problem: Problem) {
        this.problem = problem
    }

    fun getSolutionCost(): Double {
        if(cost == Double.MAX_VALUE){
            if(facilities.size == 0) return Double.MAX_VALUE

            val evaluator = Evaluator()
            this.cost = evaluator.evaluate(this, this.problem)
        }

        return cost
    }
}