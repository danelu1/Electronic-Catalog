import java.util.List;

class BestPartialScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getPartialScore();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getPartialScore()) {
				maxPartialScore = grades.get(i).getPartialScore();
				index = i;
			}
		}
		
		return grades.get(index);
	}
}