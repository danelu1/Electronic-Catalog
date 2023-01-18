import java.util.List;

class BestTotalScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getTotal();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getTotal()) {
				maxPartialScore = grades.get(i).getTotal();
				index = i;
			}
		}
		
		return grades.get(index);
	}	
}