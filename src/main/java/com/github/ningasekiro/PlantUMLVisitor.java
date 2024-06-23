package com.github.ningasekiro;


/**
 * plantUmlvisitor
 *
 * @author NingaSekiro
 * @date 2024/06/23
 */
public class PlantUMLVisitor implements Visitor {

    @Override
    public String visitOnEntry(State<?, ?> state) {
        StringBuilder sb = new StringBuilder();
        for(Transition<?, ?> transition: state.getAllTransitions()){
            sb.append(transition.getSource().getId())
                    .append(" --> ")
                    .append(transition.getTarget().getId())
                    .append(" : ")
                    .append(transition.getEvent())
                    .append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(State<?, ?> state) {
        return "";
    }
}
