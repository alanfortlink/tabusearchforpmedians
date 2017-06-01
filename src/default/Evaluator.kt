package default

/**
 * Created by alanfortlink on 5/30/17.
 */
class Evaluator{
    fun evaluate(solution: Solution, problem: Problem) : Double {
        var cost = 0.0

        for(node in problem.nodes){
            var bestFacility = solution.facilities.first()
            var bestCost = problem.distances[bestFacility.index][node.index]

            for(facility in solution.facilities){
                val newCost = problem.distances[facility.index][node.index]
                if(newCost < bestCost){
                    bestFacility = facility
                    bestCost = newCost
                }
            }

            cost += bestCost
        }

        return cost
    }

}
