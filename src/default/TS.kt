package default

/**
 * Created by alanfortlink on 5/30/17.
 */

class TS{

    enum class SearchMethod{
        FIRST_IMPROVING, BEST_IMPROVING
    };

    var problem = Problem()
    var bestSolution = Solution(Problem())
    var tabu = mutableListOf<Pair<Node, Node>>()
    var searchMethod = default.TS.SearchMethod.FIRST_IMPROVING
    var tenure = 0

    fun solve(problem: Problem, numOfIterations: Int, timeLimit: Int, tenure: Int, searchMethod: default.TS.SearchMethod) : Solution {
        this.searchMethod = searchMethod
        this.tenure = tenure
        this.problem = problem
        this.bestSolution = getBasicSolution()

        println("Starting with basic solution: ${bestSolution.getSolutionCost()}")

        for (i in 1..numOfIterations){
            println("Iteration ${i}")

            var bestNeighbor = getBestNeighbor();

            if(bestNeighbor != null && bestNeighbor.getSolutionCost() < bestSolution.getSolutionCost()){
                bestSolution = bestNeighbor

                println("New solution found on iteration ${i} : ${bestSolution.getSolutionCost()}")
            }
        }

        return bestSolution
    }

    fun getBasicSolution() : Solution {
        var bestSolution = Solution(this.problem)
        bestSolution.cost = Double.MAX_VALUE

        var facilities = mutableListOf<Node>()

        for(i in 1..this.problem.p){
            var addedNode: Node? = null
            for (node in problem.nodes){
                var solution = Solution(this.problem)
                solution.facilities = facilities.toMutableList()
                solution.facilities.add(node)

                if(solution.getSolutionCost() < bestSolution.getSolutionCost()){
                    bestSolution = solution
                    addedNode = node
                }
            }

            if(addedNode != null){
                facilities.add(addedNode)
            }
        }

        return bestSolution
    }

    fun getBestNeighbor() : Solution {
        var bestNeighbor: Solution = Solution(this.problem)
        bestNeighbor.facilities = bestSolution.facilities
        bestNeighbor.cost = bestSolution.cost

        var candidatesToLeave = bestNeighbor.facilities.toMutableList()
        var candidatesToIngress = problem.nodes.filter { !candidatesToLeave.contains(it) }

        java.util.Collections.shuffle(candidatesToLeave)
        java.util.Collections.shuffle(candidatesToIngress)

        for(candL in candidatesToLeave){
            for(candI in candidatesToIngress){
                if( bestNeighbor.facilities.contains(candI) || !bestNeighbor.facilities.contains(candL) )
                    continue
                var solution = Solution(this.problem)
                solution.facilities = bestNeighbor.facilities

                solution.facilities.remove(candL)
                solution.facilities.add(candI)

                if(!tabu.contains(Pair(candL, candI)) && solution.getSolutionCost() < bestNeighbor.getSolutionCost()){
                    bestNeighbor = solution
                    tabu.add(Pair(candI, candL))
                    if(tabu.size > tenure){
                        tabu.removeAt(0)
                    }

                    if(searchMethod == default.TS.SearchMethod.FIRST_IMPROVING){
                        return bestNeighbor
                    }
                }
            }
        }

        return bestNeighbor
    }
}

fun main(args: Array<String>) {
    var filename = "/Users/alanfortlink/Dropbox/Unicamp/mo824/atividade8/pmp_instances/instance4.pmp";

    var problem = Problem()
    problem.readFromFile(filename)

    var ts = default.TS()

    var solution = ts.solve(problem, 1000, 1000, 10, default.TS.SearchMethod.BEST_IMPROVING)
    println(solution.facilities)
    println(solution.cost)
}
