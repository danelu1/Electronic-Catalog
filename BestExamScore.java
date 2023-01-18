import java.util.List;

class BestExamScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getExamScore();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getExamScore()) {
				maxPartialScore = grades.get(i).getExamScore();
				index = i;
			}
		}
		
		return grades.get(index);
	}
}