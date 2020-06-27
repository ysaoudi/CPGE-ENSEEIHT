% Generate Random Data
N = 37;
M = confusion_matrix;
x = repmat(1:N,N,1); % generate x-coordinates
y = x'; % generate y-coordinates
% Generate Labels
t = num2cell(M); % extact values into cells
t = cellfun(@num2str, t, 'UniformOutput', false); % convert to string
% Draw Image and Label Pixels
imagesc(M)

text(x(:), y(:), t, 'HorizontalAlignment', 'Center','Color','w')